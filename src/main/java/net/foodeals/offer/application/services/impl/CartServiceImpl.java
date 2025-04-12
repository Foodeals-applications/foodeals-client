package net.foodeals.offer.application.services.impl;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import net.foodeals.offer.application.dtos.responses.CartItemResponse;
import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.product.domain.entities.Product;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.offer.application.services.CartService;
import net.foodeals.offer.domain.entities.Cart;
import net.foodeals.offer.domain.entities.CartItem;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.repositories.CartItemRepository;
import net.foodeals.offer.domain.repositories.CartRepository;
import net.foodeals.offer.domain.repositories.DealRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final DealRepository dealRepository;

    @Override
    public Cart addDealToCart(Integer userId, UUID dealId, int quantity) {

        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(userId));

        Deal deal = dealRepository.findById(dealId).orElseThrow(() -> new IllegalArgumentException("Deal not found"));

        CartItem cartItem = new CartItem(cart, deal, quantity);
        cart.getItems().add(cartItem);

        cartRepository.save(cart);
        return cart;
    }

    @Override
    public Cart getCartByUser(Integer userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }

    public void clearCart(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cartItemRepository.deleteAll(cart.getItems());
        cart.setItems(null);
        ;
        cart = cartRepository.save(cart);


    }

    @Override
    public void deleteAllDealsByOrganizationFromCart(UUID organizationId) {

        List<Cart> carts = cartRepository.findAll();

        for (Cart cart : carts) {

            List<CartItem> itemsToRemove = cart.getItems().stream()
                    .filter(cartItem -> cartItem.getDeal() != null && cartItem.getDeal().getCreator() != null
                            && organizationId.equals(cartItem.getDeal().getCreator().getId()))
                    .toList();

            cart.getItems().removeAll(itemsToRemove);

            cartItemRepository.deleteAll(itemsToRemove);
        }

    }

    @Override
    public void deleteSingleDealByOrganizationFromCart(UUID organizationId, UUID dealId) {

        List<Cart> carts = cartRepository.findAll();

        for (Cart cart : carts) {

            CartItem itemToRemove = cart.getItems().stream()
                    .filter(cartItem -> cartItem.getDeal() != null
                            && cartItem.getDeal().getCreator() != null
                            && organizationId.equals(cartItem.getDeal().getCreator().getId())
                            && dealId.equals(cartItem.getDeal().getId()))
                    .findFirst()
                    .orElse(null);

            if (itemToRemove != null) {

                cart.getItems().remove(itemToRemove);


                cartItemRepository.delete(itemToRemove);
            }
        }
    }

    @Override
    public Deal updatePrice(UUID id, BigDecimal newPrice) {
        Deal dealPro = dealRepository.findById(id).orElseThrow(() -> new RuntimeException("DealPro not found"));
        dealPro.setPrice(new Price(newPrice, Currency.getInstance("MAD")));
        return dealRepository.save(dealPro);
    }


    public  CartResponse toCartResponse(Cart cart) {

        Long deliveryCost = 0L;
        double totalDeliveryFee = cart.getItems().stream()
                .mapToDouble(cartItem -> cartItem.getDeal().getOffer().getDeliveryFee() * cartItem.getDeal().getQuantity())
                .sum();

        double totalPrice = cart.getItems().stream()
                .mapToDouble(cartItem -> cartItem.getDeal().getOffer().getSalePrice().amount().doubleValue())
                .sum();

        cart.getItems().stream()
                .mapToDouble(cartItem -> cartItem.getDeal().getOffer().getSalePrice().amount().doubleValue())
                .sum();
        List<CartItemResponse> cartItemResponses = cart.getItems().stream()
                .map(cartItem -> toCartItemResponse(cartItem)) // use map instead of forEach to return a new list
                .collect(Collectors.toList());
        return new CartResponse(deliveryCost, totalDeliveryFee, cartItemResponses);
    }


    public CartItemResponse toCartItemResponse(CartItem cartItem) {
        Product product = cartItem.getDeal().getProduct();
        CartItemResponse cartItemResponse = new CartItemResponse(product.getName(), product.getProductImagePath(),
                cartItem.getDeal().getOffer().getSalePrice().amount().doubleValue(), cartItem.getDeal().getOffer().getSubEntity().getName(),
                cartItem.getDeal().getOffer().getSubEntity().getAvatarPath(), cartItem.getDeal().getQuantity());
        return cartItemResponse;
    }


}
