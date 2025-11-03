package net.foodeals.order.application.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.*;
import net.foodeals.core.domain.enums.OrderStatus;
import net.foodeals.core.domain.enums.TransactionStatus;
import net.foodeals.core.repositories.*;
import net.foodeals.order.application.dtos.requests.ProcessPaymentRequest;
import net.foodeals.order.application.dtos.responses.ProcessPaymentResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final SubEntityRepository subEntityRepository;
    private final CouponRepository couponRepository ;

    public ProcessPaymentResponse processPayment(ProcessPaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // ⚡ Ici logique de paiement (Stripe, PayPal, etc.)
        Transaction tx = new Transaction();
        tx.setOrder(order);
        BigDecimal amount = new BigDecimal(request.getAmount().toString());
        tx.setPrice(new Price(amount, Currency.getInstance("MAD")));
        tx.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(tx);

        order.setTransaction(tx);
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        return new ProcessPaymentResponse(tx.getId(), "SUCCESS", "Payment processed successfully");
    }

    // 1️⃣
    public Map<String, Object> getPaymentData(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Regrouper par magasin
        Map<UUID, List<CartItem>> groupedByStore = cart.getItems().stream()
                .filter(CartItem::isSelected) // ⚡ seulement les produits sélectionnés
                .collect(Collectors.groupingBy(item -> item.getSubEntity().getId()));

        List<Map<String, Object>> stores = groupedByStore.entrySet().stream()
                .map(entry -> {
                    UUID storeId = entry.getKey();
                    SubEntity store = entry.getValue().get(0).getSubEntity();

                    double total = entry.getValue().stream()
                            .mapToDouble(item -> {
                                double price = item.getDeal() != null ?
                                        item.getDeal().getPrice().amount().doubleValue()
                                        : item.getProduct().getPrice().amount().doubleValue();
                                return price * item.getQuantity();
                            }).sum();

                    List<Map<String, Object>> selectedItems = entry.getValue().stream()
                            .map(item -> {
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", item.getId());
                                map.put("productId", item.getProduct().getId());
                                map.put("name", item.getProduct().getName());
                                map.put("price", item.getProduct().getPrice().amount().doubleValue());
                                map.put("discountPrice", item.getDeal() != null ?
                                        item.getDeal().getPrice().amount().doubleValue() : null);
                                map.put("quantity", item.getQuantity());
                                map.put("imageUrl", item.getProduct().getProductImagePath());
                                map.put("selected", true);
                                map.put("description", item.getProduct().getDescription());
                                map.put("collectionInfo", item.getCollectionInfo());
                                return map;
                            })
                            .collect(Collectors.toList());

                    // Les méthodes de paiement actives (fixe ou tiré de table PaymentMethod)
                    List<Map<String, Object>> paymentMethods = List.of(
                            Map.of("id", "credit_card", "name", "Credit Card", "type", "card", "icon", "credit-card", "enabled", true),
                            Map.of("id", "cash_on_delivery", "name", "Cash on Delivery", "type", "cash", "icon", "money", "enabled", true)
                    );

                    return Map.of(
                            "id", storeId,
                            "name", store.getName(),
                            "logoUrl", store.getAvatarPath(),
                            "total", total,
                            "selectedItems", selectedItems,
                            "paymentMethods", paymentMethods
                    );
                }).toList();

        // ⚡ Global methods
        List<Map<String, Object>> globalPaymentMethods = List.of(
                Map.of("id", "wallet", "name", "Digital Wallet", "type", "wallet", "icon", "wallet", "enabled", true)
        );

        return Map.of(
                "success", true,
                "data", Map.of(
                        "stores", stores,
                        "globalPaymentMethods", globalPaymentMethods
                )
        );
    }


    // 2️⃣
    @Transactional
    public Map<String, Object> selectStorePaymentMethod(Map<String, String> request) {
        UUID storeId = UUID.fromString(request.get("storeId"));
        String paymentMethodId = request.get("paymentMethodId");

        SubEntity store = subEntityRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        PaymentMethod method = paymentMethodRepository.findById(UUID.fromString(paymentMethodId))
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found"));


        // (Optionnel) Persister dans une table StorePaymentSelection si elle existe
        // storePaymentSelectionRepository.save(new StorePaymentSelection(store, method));

        return Map.of(
                "success", true,
                "message", "Payment method selected successfully",
                "data", Map.of(
                        "storeId", storeId,
                        "paymentMethodId", method.getId(),
                        "paymentMethodName", method.getLabel(),
                        "type", method.getCode()
                )
        );
    }


    @Transactional
    public Map<String, Object> applyStorePromo(Map<String, String> request) {
        UUID storeId = UUID.fromString(request.get("storeId"));
        String promoCode = request.get("promoCode");

        SubEntity store = subEntityRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        Coupon coupon = couponRepository.findByCode(promoCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid promo code"));

        if (!Boolean.TRUE.equals(coupon.getIsEnabled()) || coupon.getEndsAt().before(new Date())) {
            throw new IllegalStateException("Coupon expired or disabled");
        }

        // 🧮 Calcul du total du store
        List<CartItem> items = cartItemRepository.findBySubEntityId(storeId);
        double storeTotal = items.stream()
                .mapToDouble(item -> {
                    double price = item.getDeal() != null ?
                            item.getDeal().getPrice().amount().doubleValue() :
                            item.getProduct().getPrice().amount().doubleValue();
                    return price * item.getQuantity();
                })
                .sum();

        // 🧾 Calcul réduction
        double discount = coupon.getDiscount()!=null
                ? storeTotal * coupon.getDiscount() / 100
                : coupon.getDiscount();

        double newTotal = storeTotal - discount;

        return Map.of(
                "success", true,
                "message", "Promo code applied successfully",
                "data", Map.of(
                        "storeId", storeId,
                        "promoCode", promoCode,
                        "discountAmount", discount,
                        "discountPercentage", coupon.getDiscount()!=null? coupon.getDiscount() : null,
                        "newTotal", newTotal
                )
        );
    }


    // 4️⃣
    @Transactional
    public Map<String, Object> applyGlobalPromo(Map<String, String> request) {
        String promoCode = request.get("promoCode");

        Coupon coupon = couponRepository.findByCode(promoCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid promo code"));

        if (!Boolean.TRUE.equals(coupon.getIsEnabled()) || coupon.getEndsAt().before(new Date())) {
            throw new IllegalStateException("Coupon expired or disabled");
        }

        List<CartItem> allItems = cartItemRepository.findAll();
        double globalTotal = allItems.stream()
                .mapToDouble(item -> {
                    double price = item.getDeal() != null ?
                            item.getDeal().getPrice().amount().doubleValue() :
                            item.getProduct().getPrice().amount().doubleValue();
                    return price * item.getQuantity();
                })
                .sum();

        double discount = coupon.getDiscount()!=null
                ? globalTotal * coupon.getDiscount() / 100
                : coupon.getDiscount();

        double newTotal = globalTotal - discount;

        return Map.of(
                "success", true,
                "message", "Global promo applied successfully",
                "data", Map.of(
                        "promoCode", promoCode,
                        "discountAmount", discount,
                        "discountPercentage", coupon.getDiscount()!=null ? coupon.getDiscount() : null,
                        "originalTotal", globalTotal,
                        "newTotal", newTotal
                )
        );
    }


    // 5️⃣
    @Transactional
    public Map<String, Object> payForStore(Map<String, Object> request) {
        UUID storeId = UUID.fromString((String) request.get("storeId"));
        String paymentMethodId = (String) request.get("paymentMethodId");
        Object selectedItemsRaw = request.get("selectedItems");

        List<String> selectedItems = parseSelectedItems(selectedItemsRaw);

        List<UUID> itemIds = selectedItems.stream().map(UUID::fromString).toList();
        List<CartItem> items = cartItemRepository.findAllByIdIn(itemIds);

        double total = 0;
        for (CartItem item : items) {
            double price = item.getDeal() != null ?
                    item.getDeal().getPrice().amount().doubleValue() :
                    item.getProduct()!=null?item.getProduct().getPrice().amount().doubleValue():
                    item.getBox()!=null?item.getBox().getOffer().getSalePrice().amount().doubleValue():0;
            total += price * item.getQuantity();

            // diminuer le stock
            //item.getProduct().decreaseStock(item.getQuantity());
        }

        // Créer la commande
        Order order = new Order();
        order.setClient(userRepository.findById(items.get(0).getCart().getUserId()).get());
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        // Créer la transaction
        Transaction tx = new Transaction();
        tx.setOrder(order);
        tx.setPrice(new Price(BigDecimal.valueOf(total), Currency.getInstance("MAD")));
        tx.setStatus(TransactionStatus.COMPLETED);
        tx.setPaymentId(paymentMethodId);
        transactionRepository.save(tx);

        order.setTransaction(tx);
        orderRepository.save(order);

        // Supprimer du panier
        cartItemRepository.deleteAll(items);

        return Map.of(
                "success", true,
                "message", "Payment processed successfully",
                "data", Map.of(
                        "orderId", order.getId(),
                        "storeId", storeId,
                        "transactionId", tx.getId(),
                        "status", "completed",
                        "totalAmount", total,
                        "paymentMethod", paymentMethodId,
                        "estimatedDelivery", Instant.now().plus(2, ChronoUnit.HOURS).toString()
                )
        );
    }


    @Transactional
    public Map<String, Object> payGlobal(Map<String, Object> request) {
        String paymentMethodId = (String) request.get("paymentMethodId");
        Map<String, List<String>> selectedItemsByStore = (Map<String, List<String>>) request.get("selectedItems");

        List<Map<String, Object>> orders = new ArrayList<>();
        double totalAmount = 0;

        for (Map.Entry<String, List<String>> entry : selectedItemsByStore.entrySet()) {
            Map<String, Object> storePayRequest = Map.of(
                    "storeId", entry.getKey(),
                    "paymentMethodId", paymentMethodId,
                    "selectedItems", entry.getValue()
            );

            Map<String, Object> storePayment = payForStore(storePayRequest);
            Map<String, Object> data = (Map<String, Object>) storePayment.get("data");

            orders.add(Map.of(
                    "orderId", data.get("orderId"),
                    "storeId", data.get("storeId"),
                    "status", data.get("status"),
                    "totalAmount", data.get("totalAmount")
            ));

            totalAmount += (double) data.get("totalAmount");
        }

        return Map.of(
                "success", true,
                "message", "Global payment processed successfully",
                "data", Map.of(
                        "globalOrderId", "global_" + UUID.randomUUID(),
                        "orders", orders,
                        "totalAmount", totalAmount,
                        "paymentMethod", paymentMethodId,
                        "transactionId", "txn_" + UUID.randomUUID()
                )
        );
    }

    // 7️⃣
    public Map<String, Object> getPaymentSummary(Integer userId) {
        return Map.of(
                "success", true,
                "data", Map.of(
                        "stores", List.of(
                                Map.of(
                                        "storeId", "store1",
                                        "storeName", "Marjane Market",
                                        "subtotal", 15.50,
                                        "promoDiscount", 1.55,
                                        "deliveryFee", 0.00,
                                        "total", 13.95,
                                        "selectedItemsCount", 2,
                                        "paymentMethodSelected", true
                                )
                        ),
                        "globalSummary", Map.of(
                                "subtotal", 25.90,
                                "globalPromoDiscount", 5.18,
                                "totalDeliveryFee", 0.00,
                                "finalTotal", 20.72,
                                "totalSelectedItems", 5,
                                "loyaltyPointsEarned", 21,
                                "loyaltyPointsUsed", 0
                        )
                )
        );
    }

    // 8️⃣
    public Map<String, Object> validatePayment(Map<String, Object> request) {
        return Map.of(
                "success", true,
                "data", Map.of(
                        "valid", true,
                        "issues", List.of(),
                        "estimatedTotal", 13.95,
                        "availableItems", List.of("item1", "item2"),
                        "unavailableItems", List.of(),
                        "paymentMethodValid", true
                )
        );
    }

    // 9️⃣
    public Map<String, Object> getPaymentMethods(Integer userId) {
        return Map.of(
                "success", true,
                "data", Map.of(
                        "methods", List.of(
                                Map.of("id", "credit_card", "name", "Credit Card", "type", "card", "icon", "credit-card", "enabled", true, "supportedStores", List.of("store1", "store2"), "fees", Map.of("percentage", 0, "fixed", 0)),
                                Map.of("id", "cash_on_delivery", "name", "Cash on Delivery", "type", "cash", "icon", "money", "enabled", true, "supportedStores", List.of("store1"), "fees", Map.of("percentage", 0, "fixed", 2.00)),
                                Map.of("id", "wallet", "name", "Digital Wallet", "type", "wallet", "icon", "wallet", "enabled", true, "balance", 150.00, "supportedStores", List.of("store1", "store2"), "fees", Map.of("percentage", 0, "fixed", 0))
                        )
                )
        );
    }

    // 🔟
    public Map<String, Object> applyLoyalty(Map<String, Object> request) {
        return Map.of(
                "success", true,
                "message", "Loyalty points applied successfully",
                "data", Map.of(
                        "pointsUsed", 100,
                        "discountAmount", 5.00,
                        "remainingPoints", 850,
                        "newTotal", 8.95
                )
        );
    }


    private List<String> parseSelectedItems(Object value) {
        if (value == null) return List.of();

        // ✅ Déjà une liste
        if (value instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        }

        // ✅ Chaîne JSON ou CSV
        if (value instanceof String str) {
            String trimmed = str.trim();

            try {
                if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                    // JSON array
                    return new ObjectMapper().readValue(trimmed, new TypeReference<List<String>>() {});
                }
            } catch (Exception ignored) { }

            // CSV ou chaîne simple
            return Arrays.stream(trimmed.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        }

        throw new IllegalArgumentException("Format invalide pour 'selectedItems': " + value);
    }


}