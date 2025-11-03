package net.foodeals.order.application.services.impl;


import java.util.*;
import java.util.stream.Collectors;

import com.google.zxing.WriterException;
import net.foodeals.common.Utils.QrCodeUtil;
import net.foodeals.core.domain.entities.*;
import net.foodeals.core.domain.enums.OrderStatus;
import net.foodeals.core.domain.enums.OrderType;
import net.foodeals.core.exceptions.OrderNotFoundException;
import net.foodeals.core.repositories.*;
import net.foodeals.order.application.dtos.requests.CartItemRequest;
import net.foodeals.order.application.dtos.requests.CreateOrderRequest;
import net.foodeals.order.application.dtos.responses.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.delivery.application.services.DeliveryService;
import net.foodeals.location.application.services.AddressService;
import net.foodeals.offer.application.services.OfferService;
import net.foodeals.order.application.dtos.requests.OrderRequest;
import net.foodeals.order.application.services.CouponService;
import net.foodeals.order.application.services.OrderService;

import net.foodeals.user.application.services.UserService;
import org.springframework.stereotype.Service;

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
    private final BoxRepository boxRepository ;

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
			order.setShippingAddress(addressService.create(request.shippingAddress()))
					.setDelivery(deliveryService.create(request.delivery()));
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
		if (repository.existsById(id))
			throw new OrderNotFoundException(id);

		repository.softDelete(id);
	}


	@Override
	public Map<String, List<OrderResponse>> findOrdersByClient(User client) {
		// Récupération de toutes les commandes du client

		List<Order> orders = repository.findOrdersByClient(client.getId());

		// Création de la Map pour stocker les commandes par statut
		Map<String, List<OrderResponse>> mapOrders = new HashMap<>();

		// Filtrer et mapper les commandes par statut
		List<OrderResponse> pendingOrders = orders.stream()
				.filter(order -> OrderStatus.IN_PROGRESS.name().equalsIgnoreCase(order.getStatus().name()))
				.map(this::mapToOrderResponse)
				.collect(Collectors.toList());

		List<OrderResponse> inProgressOrders = orders.stream()
				.filter(order -> OrderStatus.COMPLETED.name().equalsIgnoreCase(order.getStatus().name()))
				.map(this::mapToOrderResponse)
				.collect(Collectors.toList());

		List<OrderResponse> canceledOrders = orders.stream()
				.filter(order -> OrderStatus.CANCELED.name().equalsIgnoreCase(order.getStatus().name()))
				.map(this::mapToOrderResponse)
				.collect(Collectors.toList());

		// Ajouter les listes dans la Map
		mapOrders.put("en attente", pendingOrders);
		mapOrders.put("livré", inProgressOrders);
		mapOrders.put("annulé", canceledOrders);

		return mapOrders;
	}

	@Override
	public OrderDetailsResponse getDetailsOrder(UUID id) throws WriterException {
		Order order =findById(id);
		if(order!=null){
         return mapToOrderDetailsResponse(order);
		}
		return null;
	}

    @Override
    public OrderConfirmationResponse getOrderConfirmation(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Transaction transaction = order.getTransaction();

        Deal deal=dealRepository.getDealByOfferId(order.getOffer().getId());
        Box box =boxRepository.getBoxByOfferId(order.getOffer().getId());
        String offerName=null;
        if(deal!=null){
            offerName=deal.getTitle();
        }
        if(box!=null){
            offerName=box.getTitle();
        }
        // Mapper Order -> OrderPaymentResponse
        OrderPaymentResponse orderDto = new OrderPaymentResponse(
                order.getId(),
                order.getStatus().name(),
                order.getQuantity(),
                offerName,
                order.getClient() != null ? order.getClient().getName().firstName()+" "+order.getClient().getName().lastName() : null,
                order.getCreatedAt()
        );

        // Mapper Transaction -> TransactionOrderResponse
        TransactionOrderResponse transactionDto = transaction != null ? new TransactionOrderResponse(
                transaction.getId(),
                transaction.getReference(),
                transaction.getPrice().amount().doubleValue(),
                transaction.getPrice().currency().getCurrencyCode(),
                transaction.getStatus().name()
        ) : null;

        return new OrderConfirmationResponse(orderDto, transactionDto);
    }

    @Override
    public CreateOrderResponse createOrder(User user, CreateOrderRequest request) {
        // 🛒 Récupérer les vrais produits depuis la DB
        double total = 0.0;
        int totalQuantity = 0;

        for (CartItemRequest item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable: " + item.getProductId()));

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
            Coupon coupon = couponRepository.findByCode(request.getPromoCode())
                    .orElseThrow(() -> new RuntimeException("Code promo invalide"));

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
        PaymentMethod paymentMethod = paymentMethodRepository.findByLabel(request.getPaymentMethod())
                .orElseThrow(() -> new RuntimeException("Méthode de paiement introuvable"));
        // ⚡ Ici tu pourrais créer une relation Order → PaymentMethod si tu l’as modélisée

        // Sauvegarde
        repository.save(order);

        return new CreateOrderResponse(order.getId(), order.getStatus().name(), total, "MAD");
    }



    private OrderResponse mapToOrderResponse(Order order) {
		UUID idProduct=UUID.randomUUID(); // TODO A VERIFIER
		Product product =productRepository.findById(idProduct).orElseThrow(EntityNotFoundException::new);
		return new OrderResponse(
				order.getId(),
				product.getName(),
				product.getProductImagePath(),
				order.getOffer().getSalePrice().amount().doubleValue(),
				order.getCreatedAt(),
				OrderType.AT_PLACE==(order.getType()) ? order.getCollectionStartTime().getHour() : 0,
				OrderType.AT_PLACE==(order.getType()) ? order.getCollectionEndTime().getHour() : 0,
				order.getTransaction().getReference()
		);

	}


	private OrderDetailsResponse mapToOrderDetailsResponse(Order order) throws WriterException {
		UUID idProduct=productRepository.findProductsWithActiveOffers(order.getOffer().getSubEntity().getId()).
				get(0).getId();
		Deal deal=dealRepository.findActiveDealByProduct(idProduct).orElse(null);
		Product product =productRepository.findById(idProduct).orElseThrow(EntityNotFoundException::new);
        String qrCodeBase64 = QrCodeUtil.generateQRCodeBase64(order.getId().toString(), 250, 250);

        return new OrderDetailsResponse(order.getId(),
				product.getName(),
				product.getDescription(),
				product.getProductImagePath(),
				order.getOffer().getSalePrice().amount().doubleValue(),
				order.getDelivery() != null ? Date.from(order.getDelivery().getCreatedAt()) : null,
				20,
				order.getOffer().getModalityPaiement(),
				deal.getId(),qrCodeBase64) ;

	}

}
