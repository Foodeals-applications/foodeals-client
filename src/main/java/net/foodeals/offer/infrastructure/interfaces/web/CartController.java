package net.foodeals.offer.infrastructure.interfaces.web;

import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.requests.CartRequest;
import net.foodeals.offer.application.dtos.responses.CartItemResponse;
import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.offer.application.services.CartService;
import net.foodeals.offer.domain.entities.Cart;
import net.foodeals.offer.domain.entities.CartItem;
import net.foodeals.user.application.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;


    @PostMapping("/add")
    public ResponseEntity<CartResponse> addDealToCart(@RequestBody CartRequest cartRequest) {
        Integer userId = userService.getConnectedUser().getId();
        Cart cart = cartService.addToCart(userId, cartRequest);
        CartResponse cartResponse = cartService.toCartResponse(cart);
        return ResponseEntity.ok(cartResponse);
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        Integer userId = userService.getConnectedUser().getId();
        Cart cart = cartService.getCartByUser(userId);
        if (cart == null) {
            return ResponseEntity.noContent().build();
        }

        CartResponse cartResponse = cartService.toCartResponse(cart);
        return ResponseEntity.ok(cartResponse);
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponse>> getCartItems() {
        Integer userId = userService.getConnectedUser().getId();
        Cart cart = cartService.getCartByUser(userId);
        if (cart == null) {
            return ResponseEntity.noContent().build();
        }

        List<CartItemResponse> response = cartService.toCartResponse(cart).getCartsItemsResponse();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/remove/{id}")
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
    }


}
