package net.foodeals.offer.application.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import net.foodeals.location.domain.entities.Address;
import net.foodeals.location.domain.repositories.AddressRepository;
import net.foodeals.offer.application.dtos.requests.CartRequest;
import net.foodeals.offer.application.dtos.responses.CartItemResponse;
import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.repositories.ProductRepository;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.UserRepository;
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
	private final BoxRepository boxRepository;
    private final UserService  userService;
    private final AddressRepository addressRepository;
	private final ProductRepository productRepository;

	@Override
	public Cart addToCart(Integer userId, CartRequest request) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        if (cart == null) {
            cart = new Cart(userId);
        }

        // 🔥 Récupérer l’adresse sélectionnée du client
        User client = userService.getConnectedUser();
        Address deliveryAddress = client.getAddress();

        // Mettre à jour les infos globales du panier
        cart.setDonation(request.isDonation());
        cart.setShowInfoDonation(request.isShowInfoDonation());
        cart.setTimeSlot(request.getTimeSlot());


        // Ajouter l’item (deal / box)
        CartItem cartItem = null;

        if (request.getDealId() != null) {
            Deal deal = dealRepository.findById(request.getDealId())
                    .orElseThrow(() -> new IllegalArgumentException("Deal not found"));
            cartItem = new CartItem(cart, deal, request.getQuantity(), request.getModalityType());
        }

        if (request.getBoxId() != null) {
            Box box = boxRepository.findById(request.getBoxId())
                    .orElseThrow(() -> new IllegalArgumentException("Box not found"));
            cartItem = new CartItem(cart, box, request.getQuantity(), request.getModalityType());
        }

        if (cartItem != null) {
            cart.getItems().add(cartItem);
        }

        return cartRepository.save(cart);
	}

    @Override
    public Cart updateCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
	public Cart getCartByUser(Integer userId) {
		return cartRepository.findByUserId(userId).orElse(null);
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
					.filter(cartItem -> cartItem.getDeal() != null && cartItem.getDeal().getCreator() != null
							&& organizationId.equals(cartItem.getDeal().getCreator().getId())
							&& dealId.equals(cartItem.getDeal().getId()))
					.findFirst().orElse(null);

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

	public CartResponse toCartResponse(Cart cart) {
		Long deliveryCost = 0L;

		double totalDeliveryFee = cart.getItems().stream().mapToDouble(cartItem -> {
			if (cartItem.getDeal() != null && cartItem.getDeal().getOffer() != null) {
				return cartItem.getDeal().getOffer().getDeliveryFee() * cartItem.getDeal().getQuantity();
			} else if (cartItem.getBox() != null && cartItem.getBox().getOffer() != null) {
				return cartItem.getBox().getOffer().getDeliveryFee() * cartItem.getBox().getQuantity();
			}
			return 0.0;
		}).sum();

		double totalPrice = cart.getItems().stream().mapToDouble(cartItem -> {
			if (cartItem.getDeal() != null && cartItem.getDeal().getOffer() != null
					&& cartItem.getDeal().getOffer().getSalePrice() != null
					&& cartItem.getDeal().getOffer().getSalePrice().amount() != null) {
				return cartItem.getDeal().getOffer().getSalePrice().amount().doubleValue();
			} else if (cartItem.getBox() != null && cartItem.getBox().getOffer() != null
					&& cartItem.getBox().getOffer().getSalePrice() != null
					&& cartItem.getBox().getOffer().getSalePrice().amount() != null) {
				return cartItem.getBox().getOffer().getSalePrice().amount().doubleValue();
			}
			return 0.0;
		}).sum();

		List<CartItemResponse> cartItemResponses = cart.getItems().stream().map(this::toCartItemResponse)
				.collect(Collectors.toList());

		return new CartResponse(deliveryCost, totalDeliveryFee, cartItemResponses);

	}

	public CartItemResponse toCartItemResponse(CartItem cartItem) {
		UUID productId=null;
        String name = null;
        String description=null;
		String imagePath = null;
        Integer totalProducts=null;
        double oldPrice = 0.0;
		double price = 0.0;
		String providerName = null;
		String providerAvatar = null;
        String address=null;
        List<ModalityPaiement>modalityPaiements=new ArrayList<>();
		int quantity = cartItem.getQuantity();

		if (cartItem.getDeal() != null && cartItem.getDeal().getOffer() != null) {
			Product product = cartItem.getDeal().getProduct();
			if (product != null) {
                productId=product.getId();
                totalProducts++;
				name = product.getName();
                description=product.getDescription();
                imagePath = product.getProductImagePath();
			}
			if (cartItem.getDeal().getOffer().getSalePrice() != null
					&& cartItem.getDeal().getOffer().getSalePrice().amount() != null) {
				price = cartItem.getDeal().getOffer().getSalePrice().amount().doubleValue();
                oldPrice = cartItem.getDeal().getOffer().getSalePrice().amount().doubleValue();
			}
			if (cartItem.getDeal().getOffer().getSubEntity() != null) {
				providerName = cartItem.getDeal().getOffer().getSubEntity().getName();
				providerAvatar = cartItem.getDeal().getOffer().getSubEntity().getAvatarPath();
                address=cartItem.getDeal().getOffer().getSubEntity().getAddress().getAddress()+ ""+cartItem.getDeal().getOffer().getSubEntity().getAddress().getCity().getName();
                modalityPaiements = cartItem.getDeal().getOffer().getSubEntity().getModalityPaiements();
			}
		} else if (cartItem.getBox() != null && cartItem.getBox().getOffer() != null) {
			name = cartItem.getBox().getTitle(); // Assurez-vous que Box a un nom
			imagePath = cartItem.getBox().getPhotoBoxPath(); // Assurez-vous que Box a une image
			if (cartItem.getBox().getOffer().getSalePrice() != null
					&& cartItem.getBox().getOffer().getSalePrice().amount() != null) {
				price = cartItem.getBox().getOffer().getSalePrice().amount().doubleValue();
                oldPrice = cartItem.getBox().getOffer().getPrice().amount().doubleValue();
			}
			if (cartItem.getBox().getOffer().getSubEntity() != null) {
				providerName = cartItem.getBox().getOffer().getSubEntity().getName();
				providerAvatar = cartItem.getBox().getOffer().getSubEntity().getAvatarPath();
                address=cartItem.getBox().getOffer().getSubEntity().getAddress().getAddress()+ ""+cartItem.getBox().
                        getOffer().getSubEntity().getAddress().getCity().getName();
                modalityPaiements = cartItem.getBox().getOffer().getSubEntity().getModalityPaiements();
			}
		} else if (cartItem.getProduct() != null) {
			Product product = cartItem.getProduct();
            productId=product.getId();
            totalProducts++;
			name = product.getName();
			imagePath = product.getProductImagePath();
            description=product.getDescription();

			// À adapter si Product contient des infos de prix et de fournisseur
		}

		return new CartItemResponse(cartItem.getId(),productId,name, description,imagePath, totalProducts,price,oldPrice, providerName,
             modalityPaiements,   providerAvatar, quantity);
	}

}
