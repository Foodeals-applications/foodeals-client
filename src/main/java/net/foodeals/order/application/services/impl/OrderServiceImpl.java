package net.foodeals.order.application.services.impl;


import com.google.zxing.WriterException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.Utils.QrCodeUtil;
import net.foodeals.core.domain.entities.*;
import net.foodeals.core.domain.enums.OrderStatus;
import net.foodeals.core.domain.enums.OrderType;
import net.foodeals.core.exceptions.OrderNotFoundException;
import net.foodeals.core.repositories.*;
import net.foodeals.delivery.application.services.DeliveryService;
import net.foodeals.location.application.services.AddressService;
import net.foodeals.offer.application.services.OfferService;
import net.foodeals.order.application.dtos.requests.CartItemRequest;
import net.foodeals.order.application.dtos.requests.CreateOrderRequest;
import net.foodeals.order.application.dtos.requests.OrderRequest;
import net.foodeals.order.application.dtos.responses.*;
import net.foodeals.order.application.services.CouponService;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.user.application.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    private final CouponService couponService;
    private final AddressService addressService;
    private final UserService userService;
    private final OfferService offerService;
    private final DeliveryService deliveryService;
    private final DealRepository dealRepository;
    private final BoxRepository boxRepository;

    private final PaymentMethodRepository paymentMethodRepository;
    private final CouponRepository couponRepository;
    private final AddressRepository addressRepository;
    private final OfferRepository offerRepository;   // ⚡ pour récupérer les produits/offres réelles
    private final CartRepository cartRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final ProductRepository productRepository;

    @Value("${upload.directory}")
    private String uploadDir;

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Order> findAll(Integer pageNumber, Integer pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public Order findById(UUID id) {
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (!order.isSeen()) {
            order.setSeen(true);
        }
        return order;
    }

    @Override
    public Order create(OrderRequest request) {
        final User client = userService.findById(request.clientId());
        final Offer offer = offerService.findById(request.offerId());

        final Order order = Order.create(request.type(), request.status(), client, offer);

        if (request.type().equals(OrderType.DELIVERY)) {
            order.setShippingAddress(addressService.create(request.shippingAddress())).setDelivery(deliveryService.create(request.delivery()));
        }

        if (request.couponId() != null) {
            order.setCoupon(couponService.findById(request.couponId()));
        }

        return repository.save(order);
    }

    @Override
    public Order update(UUID id, OrderRequest request) {
        return null;
    }

    @Override
    public void delete(UUID id) {
        if (repository.existsById(id)) throw new OrderNotFoundException(id);

        repository.softDelete(id);
    }


    @Override
    public Map<String, List<OrderResponse>> findOrdersByClient(User client) {
        // Récupération de toutes les commandes du client

        List<Order> orders = repository.findOrdersByClient(client.getId());

        // Création de la Map pour stocker les commandes par statut
        Map<String, List<OrderResponse>> mapOrders = new HashMap<>();

        // Filtrer et mapper les commandes par statut
        List<OrderResponse> pendingOrders = orders.stream().filter(order -> OrderStatus.IN_PROGRESS.name().equalsIgnoreCase(order.getStatus().name())).map(this::mapToOrderResponse).collect(Collectors.toList());

        List<OrderResponse> inProgressOrders = orders.stream().filter(order -> OrderStatus.COMPLETED.name().equalsIgnoreCase(order.getStatus().name())).map(this::mapToOrderResponse).collect(Collectors.toList());

        List<OrderResponse> canceledOrders = orders.stream().filter(order -> OrderStatus.CANCELED.name().equalsIgnoreCase(order.getStatus().name())).map(this::mapToOrderResponse).collect(Collectors.toList());

        // Ajouter les listes dans la Map
        mapOrders.put("en attente", pendingOrders);
        mapOrders.put("livré", inProgressOrders);
        mapOrders.put("annulé", canceledOrders);

        return mapOrders;
    }

    @Override
    public OrderDetailsResponse getDetailsOrder(UUID id) throws WriterException {
        Order order = findById(id);
        if (order != null) {
            return mapToOrderDetailsResponse(order);
        }
        return null;
    }

    @Override
    public OrderConfirmationResponse getOrderConfirmation(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        Transaction transaction = order.getTransaction();

        Deal deal = dealRepository.getDealByOfferId(order.getOffer().getId());
        Box box = boxRepository.getBoxByOfferId(order.getOffer().getId());
        String offerName = null;
        if (deal != null) {
            offerName = deal.getTitle();
        }
        if (box != null) {
            offerName = box.getTitle();
        }
        // Mapper Order -> OrderPaymentResponse
        OrderPaymentResponse orderDto = new OrderPaymentResponse(order.getId(), order.getStatus().name(), order.getQuantity(), offerName, order.getClient() != null ? order.getClient().getName().firstName() + " " + order.getClient().getName().lastName() : null, order.getCreatedAt());

        // Mapper Transaction -> TransactionOrderResponse
        TransactionOrderResponse transactionDto = transaction != null ? new TransactionOrderResponse(transaction.getId(), transaction.getReference(), transaction.getPrice().amount().doubleValue(), transaction.getPrice().currency().getCurrencyCode(), transaction.getStatus().name()) : null;

        return new OrderConfirmationResponse(orderDto, transactionDto);
    }

    @Override
    public CreateOrderResponse createOrder(User user, CreateOrderRequest request) {
        // 🛒 Récupérer les vrais produits depuis la DB
        double total = 0.0;
        int totalQuantity = 0;

        for (CartItemRequest item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow(() -> new RuntimeException("Produit introuvable: " + item.getProductId()));

            double price = product.getPrice() != null ? product.getPrice().amount().doubleValue() : 0.0;
            total += price * item.getQuantity();
            totalQuantity += item.getQuantity();

            // ✅ Optionnel: sauvegarder le panier
            Cart cart = new Cart();
            cart.setUserId(user.getId());
            cartRepository.save(cart);
        }

        // 🎟️ Vérifier promo
        if (request.getPromoCode() != null) {
            Coupon coupon = couponRepository.findByCode(request.getPromoCode()).orElseThrow(() -> new RuntimeException("Code promo invalide"));

            if (coupon.getEndsAt().after(new Date()) && Boolean.TRUE.equals(coupon.getIsEnabled())) {
                // Exemple simple: réduction en %
                total = total - (total * (coupon.getDiscount() / 100));
            }
        }

        // 📦 Créer l’entité Order
        Order order = new Order();
        order.setClient(user);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setQuantityOfOrder(totalQuantity);

        // 🏠 Sauvegarder l’adresse
        Address address = new Address();
        address.setAddress(request.getDeliveryAddress().address());
        address.setCity(cityRepository.findById(request.getDeliveryAddress().cityId()).orElse(null));
        address.setZip(request.getDeliveryAddress().zip());
        address.setCountry(countryRepository.findByName("Morocco"));
        addressRepository.save(address);
        order.setShippingAddress(address);

        // 💳 Associer le moyen de paiement
        PaymentMethod paymentMethod = paymentMethodRepository.findByLabel(request.getPaymentMethod()).orElseThrow(() -> new RuntimeException("Méthode de paiement introuvable"));
        // ⚡ Ici tu pourrais créer une relation Order → PaymentMethod si tu l’as modélisée

        // Sauvegarde
        repository.save(order);

        return new CreateOrderResponse(order.getId(), order.getStatus().name(), total, "MAD");
    }


    private OrderResponse mapToOrderResponse(Order order) {
        UUID idProduct=null;
        Deal deal = dealRepository.getDealByOfferId(order.getOffer().getId());
        if(deal != null) {
            idProduct = deal.getProduct().getId();
        }
        Box box = boxRepository.getBoxByOfferId(order.getOffer().getId());
        if(box != null) {
            idProduct =box.getProducts().get(0).getId();
        }

        Product product = productRepository.findById(idProduct).orElseThrow(EntityNotFoundException::new);
        return new OrderResponse(order.getId(), product.getName(), product.getProductImagePath(), order.getOffer().getSalePrice().amount().doubleValue(), order.getCreatedAt(), OrderType.AT_PLACE == (order.getType()) ? order.getCollectionStartTime().getHour() : 0, OrderType.AT_PLACE == (order.getType()) ? order.getCollectionEndTime().getHour() : 0, order.getTransaction().getReference());

    }


    private OrderDetailsResponse mapToOrderDetailsResponse(Order order) throws WriterException {

        String qrCodeBase64 = QrCodeUtil.generateQRCodeBase64(order.getId().toString(), 250, 250);
        Deal deal = dealRepository.getDealByOfferId(order.getOffer().getId());
        Box box = boxRepository.getBoxByOfferId(order.getOffer().getId());

        OrderDetailsResponse response = new OrderDetailsResponse();

        // --- Informations de livraison estimée ---
        response.setEstimatedDeliveryTime("20:45");
        response.setEstimatedDeliveryDate(order.getDelivery() != null ? Date.from(order.getDelivery().getCreatedAt()) : null);
        response.setDeliveryDateFormatted("Mer, 23 Mars");

        // --- Livreurs ---
        OrderDetailsResponse.DeliveryPersonResponse deliveryPerson = new OrderDetailsResponse.DeliveryPersonResponse();
        if (order.getDelivery() != null) {
            deliveryPerson.setId(order.getDelivery().getDeliveryBoy().getId().toString());
            deliveryPerson.setName(order.getDelivery().getDeliveryBoy().getName().firstName() + " " + order.getDelivery().getDeliveryBoy().getName().lastName());
            deliveryPerson.setDistance(order.getDelivery().getRating().toString()); // à calculer si possible
            deliveryPerson.setImage(order.getDelivery().getDeliveryBoy().getAvatarPath());
            deliveryPerson.setPhone(order.getDelivery().getDeliveryBoy().getPhone());
            deliveryPerson.setRating(order.getDelivery().getRating());
            deliveryPerson.setVehicleType("MOTO");
        }
        response.setDeliveryPerson(deliveryPerson);

        // --- Adresse de livraison ---
        OrderDetailsResponse.DeliveryAddressResponse address = new OrderDetailsResponse.DeliveryAddressResponse();
        if (order.getDelivery() != null) {
            address.setFullAddress(order.getClient() != null ? order.getClient().getAddress().getAddress() : order.getClientPro().getAddress().getAddress());
            address.setStreet(order.getClient() != null ? order.getClient().getAddress().getAddress() : order.getClientPro().getAddress().getAddress());
            address.setStreetNumber(order.getClient() != null ? order.getClient().getAddress().getAddress() : order.getClientPro().getAddress().getAddress());
            address.setCity(order.getClient() != null ? order.getClient().getAddress().getCity().getName() : order.getClientPro().getAddress().getCity().getName());
            address.setPostalCode(order.getClient() != null ? order.getClient().getAddress().getZip() : order.getClientPro().getAddress().getZip());
            address.setCountry(order.getClient() != null ? order.getClient().getAddress().getCountry().getName() : order.getClientPro().getAddress().getCountry().getName());
            address.setAdditionalInfo(order.getAdditionalInformation());

            OrderDetailsResponse.DeliveryAddressResponse.Coordinates coords = null;
            if (order.getClient() != null && order.getClient().getAddress() != null) {
                coords = new OrderDetailsResponse.DeliveryAddressResponse.Coordinates(
                        order.getClient().getAddress().getCoordinates().latitude(),
                        order.getClient().getAddress().getCoordinates().longitude()
                );
            } else if (order.getClientPro() != null && order.getClientPro().getAddress() != null) {
                coords = new OrderDetailsResponse.DeliveryAddressResponse.Coordinates(
                        order.getClientPro().getAddress().getCoordinates().latitude(),
                        order.getClientPro().getAddress().getCoordinates().longitude()
                );
            }

            address.setCoordinates(coords);
        } response.setDeliveryAddress(address);

        // --- Produits commandés ---

        List<OrderDetailsResponse.ItemResponse> items = new ArrayList<>();
        if (deal != null) {

            OrderDetailsResponse.ItemResponse i = new OrderDetailsResponse.ItemResponse();
            i.setId(UUID.randomUUID().toString());
            i.setProductId(deal.getProduct().getId().toString());
            i.setName(deal.getProduct().getName());
            i.setQuantity(deal.getQuantity());
            i.setPrice(deal.getOffer().getPrice().amount().doubleValue());
            i.setOriginalPrice(deal.getProduct().getPrice().amount().doubleValue());
            i.setImage(deal.getProduct().getProductImagePath());
            i.setThumbnail("thumbnail path");
            i.setNotes("note");
            items.add(i);
        }
        if (box != null) {
            box.getProducts().stream().forEach(p -> {
                OrderDetailsResponse.ItemResponse i = new OrderDetailsResponse.ItemResponse();
                i.setId(UUID.randomUUID().toString());
                i.setProductId(p.getId().toString());
                i.setName(p.getName());
                i.setQuantity(box.getQuantity());
                i.setPrice(box.getOffer().getPrice().amount().doubleValue());
                i.setOriginalPrice(p.getPrice().amount().doubleValue());
                i.setImage(p.getProductImagePath());
                i.setThumbnail("thumbnail path");
                i.setNotes("note");
                items.add(i);
            });
        }

        response.setItems(items);

        // --- Paiement ---
        OrderDetailsResponse.PaymentResponse payment = new OrderDetailsResponse.PaymentResponse();
        payment.setMethod(order.getOffer().getModalityPaiement().name());
        payment.setStatus(order.getTransaction().getStatus().name());
        payment.setCardLastFourDigits(null);
        payment.setCardBrand(null);
        payment.setTransactionId(order.getTransaction().getReference());
        payment.setAmount(order.getOffer().getSalePrice().amount().doubleValue());
        payment.setCurrency(order.getOffer().getSalePrice().currency().getCurrencyCode());
        payment.setPaidAt(Date.from(order.getTransaction().getCreatedAt()));
        response.setPayment(payment);

        // --- Statut + historique ---
        response.setStatus(order.getStatus().name());
        response.setStatusHistory(order.getStatusHistory().stream().map(h -> new OrderDetailsResponse.StatusHistoryResponse(h.getStatus().name(), Date.from(h.getTimestamp()))).toList());

        // --- Totaux ---
        OrderDetailsResponse.TotalsResponse totals = new OrderDetailsResponse.TotalsResponse();
        totals.setSubtotal(order.getSubtotal());
        totals.setDeliveryFee(order.getOffer().getDeliveryFee());
        totals.setTax(order.getTax());
        totals.setDiscount(order.getDiscount());
        totals.setTotal(order.getTotal());
        response.setTotals(totals);
        return response;
    }
}
