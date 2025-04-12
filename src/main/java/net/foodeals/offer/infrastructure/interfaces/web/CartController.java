package net.foodeals.offer.infrastructure.interfaces.web;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.offer.application.services.CartService;
import net.foodeals.offer.domain.entities.Cart;
import net.foodeals.user.application.services.UserService;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;
	private final UserService userService ;


	@PostMapping("/add")
	public ResponseEntity<CartResponse> addDealToCart(@RequestParam UUID dealId,
			@RequestParam int quantity) {
		Integer userId=userService.getConnectedUser().getId();
		Cart cart = cartService.addDealToCart(userId, dealId, quantity);
		CartResponse cartResponse = cartService.toCartResponse(cart);
		return ResponseEntity.ok(cartResponse);
	}

	@GetMapping
	public ResponseEntity<CartResponse> getCart() {
		Integer userId=userService.getConnectedUser().getId();
		Cart cart = cartService.getCartByUser(userId);
		if(cart==null){
			return ResponseEntity.noContent().build();
		}
		CartResponse cartResponse = cartService.toCartResponse(cart);
		return ResponseEntity.ok(cartResponse);
	}

	@DeleteMapping
	public ResponseEntity<Void> clearCart() {
		Integer userId=userService.getConnectedUser().getId();
		cartService.clearCart(userId);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}/price")
    public ResponseEntity<Void> updatePrice(@PathVariable UUID id, @RequestParam BigDecimal newPrice) {
        cartService.updatePrice(id, newPrice);
        return ResponseEntity.noContent().build();
    }
	
	@DeleteMapping("/deleteSingleDealPro/{organizationId}/{deletePro}")
    public ResponseEntity<Void> deleteAllDealsByOrganization(@PathVariable UUID organizationId,@PathVariable UUID dealProId) {
        cartService.deleteSingleDealByOrganizationFromCart(organizationId, dealProId);
        return ResponseEntity.noContent().build();
    }
	

}
