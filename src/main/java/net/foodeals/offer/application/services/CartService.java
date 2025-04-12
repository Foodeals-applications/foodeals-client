package net.foodeals.offer.application.services;

import java.math.BigDecimal;
import java.util.UUID;

import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.offer.domain.entities.Cart;
import net.foodeals.offer.domain.entities.Deal;

public interface CartService {

	 Cart addDealToCart(Integer userId, UUID dealId, int quantity);
	 
	 Cart getCartByUser(Integer  userId);
	 
	 void clearCart(Integer userId);

	void deleteAllDealsByOrganizationFromCart(UUID organizationId);
	
	void deleteSingleDealByOrganizationFromCart(UUID organizationId, UUID dealId) ;

	Deal updatePrice(UUID id, BigDecimal newPrice);

	public CartResponse toCartResponse(Cart cart);
}
