package net.foodeals.offer.infrastructure.interfaces.web;

import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.Cart;
import net.foodeals.core.domain.entities.User;
import net.foodeals.offer.application.dtos.requests.CartRequest;
import net.foodeals.offer.application.dtos.requests.SelectItemRequest;
import net.foodeals.offer.application.dtos.requests.SelectStoreItemsRequest;
import net.foodeals.offer.application.dtos.requests.UpdateQuantityRequest;
import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.offer.application.dtos.responses.RemoveItemResponse;
import net.foodeals.offer.application.dtos.responses.SelectItemResponse;
import net.foodeals.offer.application.dtos.responses.UpdateQuantityResponse;
import net.foodeals.offer.application.services.CartService;
import net.foodeals.order.application.dtos.responses.CheckoutDataResponse;
import net.foodeals.order.application.services.CheckoutService;
import net.foodeals.user.application.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/validate")
    public Map<String, Object> validateCart(@RequestBody Map<String, List<UUID>> request) {
        List<UUID> selectedItems = request.get("selectedItems");
        return cartService.validateCart(selectedItems);
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
        UpdateQuantityResponse response = cartService.updateItemQuantity(request.getItemId(), request.getQuantity());

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

        RemoveItemResponse response = cartService.removeItem(itemId);

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

        SelectItemResponse response = cartService.selectItem(request.getItemId(), request.isSelected());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Item selection updated successfully",
                "data", Map.of(
                        "itemId", response.getData().getItemId(),
                        "selected", response.getData().isSelected()
                )
        ));
    }


    @PutMapping("/store/select-all/{storeId}")
    public Map<String, Object> selectAll(@PathVariable UUID storeId, @RequestParam boolean selected) {
        return cartService.selectAllStore(storeId, selected);
    }

    @DeleteMapping("/store/{storeId}")
    public Map<String, Object> removeAll(@PathVariable UUID storeId) {
        return cartService.removeAllStore(storeId);
    }

    @DeleteMapping("/clear")
    public Map<String, Object> clear() {
        Integer userId=userService.getConnectedUser().getId();
        return cartService.clearCart(userId);
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        Integer userId=userService.getConnectedUser().getId();
        return cartService.getSummary(userId);
    }


}
