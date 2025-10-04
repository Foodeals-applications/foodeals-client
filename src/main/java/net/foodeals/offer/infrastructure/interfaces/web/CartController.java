package net.foodeals.offer.infrastructure.interfaces.web;

import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.requests.CartRequest;
import net.foodeals.offer.application.dtos.requests.SelectItemRequest;
import net.foodeals.offer.application.dtos.requests.UpdateQuantityRequest;
import net.foodeals.offer.application.dtos.responses.*;
import net.foodeals.offer.application.services.CartService;
import net.foodeals.offer.domain.entities.Cart;
import net.foodeals.offer.domain.entities.CartItem;
import net.foodeals.order.application.dtos.responses.CheckoutDataResponse;
import net.foodeals.order.application.services.CheckoutService;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final CheckoutService checkoutService;

    @GetMapping("/checkout-data")
    public ResponseEntity<CheckoutDataResponse> getCheckoutData() {
        CheckoutDataResponse response = checkoutService.getCheckoutData();
        return ResponseEntity.ok(response);
    }

   @PostMapping("/add")
    public ResponseEntity<CartResponse> addDealToCart(@RequestBody CartRequest cartRequest) {
        Integer userId = userService.getConnectedUser().getId();
        Cart cart = cartService.addToCart(userId, cartRequest);
        CartResponse cartResponse = cartService.toCartResponse(cart);
        return ResponseEntity.ok(cartResponse);
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        User user = userService.getConnectedUser();
        return ResponseEntity.ok(cartService.getUserCart(user.getId()));
    }

    @PutMapping("/item/quantity")
    public ResponseEntity<?> updateItemQuantity(@RequestBody UpdateQuantityRequest request) {
        User user = userService.getConnectedUser();
        UpdateQuantityResponse response=cartService.updateItemQuantity(request.getItemId(), request.getQuantity());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item quantity updated successfully",
                "data", Map.of(
                        "productId", response.getData().getItemId()),
                        "quantity", response.getData().getQuantity(),
                        "totalPrice", response.getData().getTotalPrice()
                )
        );
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable UUID itemId) {

        RemoveItemResponse response =cartService.removeItem(itemId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item removed from cart successfully",
                "data", Map.of(
                        "removedItemId", response.getData().getRemovedItemId(),
                        "updatedTotal", response.getData().getUpdatedTotal()
                )
        ));
    }

    @PutMapping("/item/select")
    public ResponseEntity<?> selectItem(@RequestBody SelectItemRequest request) {

        SelectItemResponse response=cartService.selectItem(request.getItemId(), request.isSelected());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item selection updated successfully",
                "data", Map.of(
                        "itemId", response.getData().getItemId(),
                        "selected", response.getData().isSelected()
                )
        ));
    }

/*@PutMapping("/api/cart/store/select-all")
    public ResponseEntity<?> selectAllStoreItems(@RequestBody SelectStoreItemsRequest request, @AuthenticationPrincipal User user) {
        List<CartItem> items = cartItemRepository.findByCartUserIdAndSubEntityId(user.getId(), UUID.fromString(request.getStoreId()));

        items.forEach(i -> i.setSelected(request.isSelected()));
        cartItemRepository.saveAll(items);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "All store items selection updated successfully",
                "data", Map.of(
                        "storeId", request.getStoreId(),
                        "selectedItemsCount", items.stream().filter(CartItem::isSelected).count(),
                        "totalItems", items.size()
                )
        ));
    }

    @DeleteMapping("/api/cart/store/{storeId}")
    public ResponseEntity<?> removeStoreItems(@PathVariable UUID storeId, @AuthenticationPrincipal User user) {
        List<CartItem> items = cartItemRepository.findByCartUserIdAndSubEntityId(user.getId(), storeId);
        int removedCount = items.size();
        cartItemRepository.deleteAll(items);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "All items removed from store successfully",
                "data", Map.of(
                        "storeId", storeId,
                        "removedItemsCount", removedCount
                )
        ));
    }
    @DeleteMapping("/api/cart/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        int removedItems = cart.getItems().size();
        int removedStores = (int) cart.getItems().stream().map(i -> i.getSubEntity().getId()).distinct().count();

        cartItemRepository.deleteAll(cart.getItems());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cart cleared successfully",
                "data", Map.of(
                        "removedItemsCount", removedItems,
                        "removedStoresCount", removedStores
                )
        ));
    }
    @PostMapping("/api/cart/item")
    public ResponseEntity<?> addItem(@RequestBody AddItemRequest request, @AuthenticationPrincipal User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(new Cart(user)));

        Product product = productRepository.findById(UUID.fromString(request.getProductId()))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = new CartItem(cart, product, request.getQuantity(), ModalityType.PICKUP);
        item.setSubEntity(product.getSubEntity());
        item.setSelected(true);

        cartItemRepository.save(item);

        double totalPrice = product.getPrice().amount().doubleValue() * request.getQuantity();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item added to cart successfully",
                "data", Map.of(
                        "itemId", item.getId(),
                        "productId", product.getId(),
                        "quantity", item.getQuantity(),
                        "totalPrice", totalPrice
                )
        ));
    }
    @GetMapping("/api/cart/summary")
    public ResponseEntity<?> getCartSummary(@AuthenticationPrincipal User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        int totalItems = cart.getItems().size();
        int selectedItems = (int) cart.getItems().stream().filter(CartItem::isSelected).count();

        double totalPrice = cart.getItems().stream().mapToDouble(i -> i.getQuantity() * i.getPriceValue()).sum();
        double selectedPrice = cart.getItems().stream().filter(CartItem::isSelected).mapToDouble(i -> i.getQuantity() * i.getPriceValue()).sum();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                        "totalItems", totalItems,
                        "selectedItems", selectedItems,
                        "totalPrice", totalPrice,
                        "selectedPrice", selectedPrice,
                        "storesCount", cart.getItems().stream().map(i -> i.getSubEntity().getId()).distinct().count(),
                        "deliveryFee", 0.0,
                        "finalTotal", selectedPrice
                )
        ));
    }
    @PostMapping("/api/cart/validate")
    public ResponseEntity<?> validateCart(@RequestBody ValidateCartRequest request, @AuthenticationPrincipal User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> selectedItems = cart.getItems().stream()
                .filter(i -> request.getSelectedItems().contains(i.getId().toString()))
                .toList();

        // ⚡️ Check availability & prices
        List<String> unavailableItems = selectedItems.stream()
                .filter(i -> i.getProduct() != null && !i.getProduct().isAvailable())
                .map(i -> i.getId().toString())
                .toList();

        List<String> priceChanges = new ArrayList<>();
        double total = 0.0;

        for (CartItem item : selectedItems) {
            double currentPrice = item.getPriceValue();
            total += currentPrice * item.getQuantity();
            // tu peux comparer avec une snapshot de prix stockée au moment de l’ajout
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                        "valid", unavailableItems.isEmpty() && priceChanges.isEmpty(),
                        "unavailableItems", unavailableItems,
                        "priceChanges", priceChanges,
                        "total", total
                )
        ));
    }


   /* @GetMapping("/items")
    public ResponseEntity<List<CartItemResponse>> getCartItems() {
        Integer userId = userService.getConnectedUser().getId();
        Cart cart = cartService.getCartByUser(userId);
        if (cart == null) {
            return ResponseEntity.noContent().build();
        }

        List<CartItemResponse> response = cartService.toCartResponse(cart).getCartsItemsResponse();
        return ResponseEntity.ok(response);
    }*/

  /*  @DeleteMapping("/items/remove/{id}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable UUID id) {
        Integer userId = userService.getConnectedUser().getId();
        Cart cart = cartService.getCartByUser(userId);
        if (cart == null) {
            return ResponseEntity.noContent().build();
        }

        cart.getItems().stream().filter(item -> item.getId().equals(id)).findFirst().ifPresent(item -> {
            cart.getItems().remove(item);
        });

        Cart updated = cartService.updateCart(cart);
        CartResponse cartResponse = cartService.toCartResponse(updated);
        return ResponseEntity.ok(cartResponse);
    }

    @PutMapping("/items/quantity")
    public ResponseEntity<CartResponse> updateQuantity(UUID productId, Integer quantity) {
        Integer userId = userService.getConnectedUser().getId();
        Cart cart = cartService.getCartByUser(userId);
        if (cart == null) {
            return ResponseEntity.noContent().build();
        }

        CartItem item = cart.getItems().stream().filter(cartItem -> cartItem.getProduct().getId().equals(productId)).findFirst().orElse(null);

        if (item == null) {
            return ResponseEntity.notFound().build(); // ou autre gestion d'erreur
        }

        item.setQuantity(quantity);
        cartService.updateCart(cart); // ou updateCart(cart), selon ton service

        CartResponse cartResponse = cartService.toCartResponse(cart);
        return ResponseEntity.ok(cartResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        Integer userId = userService.getConnectedUser().getId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<Void> updatePrice(@PathVariable UUID id, @RequestParam BigDecimal newPrice) {
        cartService.updatePrice(id, newPrice);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteSingleDealPro/{organizationId}/{deletePro}")
    public ResponseEntity<Void> deleteAllDealsByOrganization(@PathVariable UUID organizationId, @PathVariable UUID dealProId) {
        cartService.deleteSingleDealByOrganizationFromCart(organizationId, dealProId);
        return ResponseEntity.noContent().build();
    } */


}
