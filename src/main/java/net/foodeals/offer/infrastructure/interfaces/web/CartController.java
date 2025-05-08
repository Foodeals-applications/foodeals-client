package net.foodeals.offer.infrastructure.interfaces.web;

import java.math.BigDecimal;
import java.util.UUID;

import net.foodeals.offer.application.dtos.requests.CartRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<CartResponse> addDealToCart(@RequestBody CartRequest cartRequest) {
		Integer userId=userService.getConnectedUser().getId();
		Cart cart = cartService.addToCart(userId, cartRequest);
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
