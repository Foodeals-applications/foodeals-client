package net.foodeals.offer.application.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.foodeals.core.domain.entities.Cart;
import net.foodeals.core.domain.entities.Deal;
import net.foodeals.offer.application.dtos.requests.CartRequest;
import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.offer.application.dtos.responses.RemoveItemResponse;
import net.foodeals.offer.application.dtos.responses.SelectItemResponse;
import net.foodeals.offer.application.dtos.responses.UpdateQuantityResponse;

public interface CartService {

	 Cart addToCart(Integer userId, CartRequest request);

     Cart updateCart(Cart cart);
	 
	 Cart getCartByUser(Integer  userId);

     Map<String, Object> clearCart(Integer userId);

	void deleteAllDealsByOrganizationFromCart(UUID organizationId);
	
	void deleteSingleDealByOrganizationFromCart(UUID organizationId, UUID dealId) ;

    public Map<String, Object> validateCart(List<UUID> selectedItems);

	Deal updatePrice(UUID id, BigDecimal newPrice);

    CartResponse getUserCart(Integer userId);
    public SelectItemResponse selectItem(UUID itemId, boolean selected);
    public RemoveItemResponse removeItem(UUID itemId);
    public UpdateQuantityResponse updateItemQuantity(UUID itemId, int quantity);
    public Map<String, Object> selectAllStore(UUID storeId, boolean selected);
    public Map<String, Object> removeAllStore(UUID storeId);
    public Map<String, Object> getSummary(Integer userId);


    CartResponse toCartResponse(Cart cart);
}
