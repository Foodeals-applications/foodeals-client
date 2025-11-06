package net.foodeals.offer.application.services.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import net.foodeals.core.domain.entities.*;
import net.foodeals.core.repositories.*;
import net.foodeals.offer.application.dtos.requests.CartRequest;
import net.foodeals.offer.application.dtos.responses.*;
import net.foodeals.user.application.services.UserService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.services.CartService;


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

    @Override
    public CartResponse getUserCart(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));


        // ⚡ On regroupe les items par "store"
        Map<UUID, List<CartItem>> groupedByStore = cart.getItems().stream().filter(item->item.getDeletedAt()==null)
                .collect(Collectors.groupingBy(item -> item.getSubEntity().getId()));

        List<StoreCartResponse> stores = groupedByStore.entrySet().stream()
                .map(entry -> {
                    UUID storeId = entry.getKey();
                    SubEntity store = entry.getValue().get(0).getSubEntity();

                    List<CartItemResponse> itemResponses = entry.getValue().stream().map(item -> {
                        UUID productId = null;
                        String name = null;
                        String description = null;
                        String imagePath = null;
                        Double price = null;
                        Double discountPrice = null;

                        if (item.getProduct() != null) {
                            productId = item.getProduct().getId();
                            name = item.getProduct().getName();
                            description = item.getProduct().getDescription();
                            imagePath = item.getProduct().getProductImagePath();
                            price = item.getProduct().getPrice().amount().doubleValue();
                        } else if (item.getDeal() != null) {
                            productId = item.getDeal().getId(); // ⚡ on met l’ID du deal
                            name = item.getDeal().getTitle();
                            description = item.getDeal().getDescription();
                            imagePath = item.getDeal().getProduct().getProductImagePath();
                            price = item.getDeal().getProduct().getPrice().amount().doubleValue();
                            discountPrice = item.getDeal().getPrice().amount().doubleValue();
                        } else if (item.getBox() != null) {
                            productId = item.getBox().getId(); // ⚡ on met l’ID du box
                            name = item.getBox().getTitle();
                            description = item.getBox().getDescription();
                            imagePath = item.getBox().getPhotoBoxPath();
                            price = item.getBox().getOffer().getPrice().amount().doubleValue();
                            discountPrice = item.getBox().getOffer().getSalePrice().amount().doubleValue();
                        }

                        return new CartItemResponse(
                                item.getId(),
                                productId,
                                name,
                                name, // titre aussi dans "shortName"
                                price,
                                discountPrice,
                                item.getQuantity(),
                                imagePath,
                                storeId,
                                true, // sélectionné par défaut
                                description,
                                item.getCollectionInfo()
                        );
                    }).toList();

                    Double total = itemResponses.stream()
                            .mapToDouble(i -> (i.getDiscountPrice() != null ? i.getDiscountPrice() : i.getPrice()) * i.getQuantity())
                            .sum();

                    return new StoreCartResponse(
                            storeId,
                            store.getName(),
                            store.getAvatarPath(),
                            total,
                            itemResponses
                    );
                }).toList();

        return new CartResponse(true, new CartData(stores));
    }


    @Override
    public CartResponse toCartResponse(Cart cart) {

        // ⚡ On regroupe les items par "store"
        Map<UUID, List<CartItem>> groupedByStore = cart.getItems().stream()
                .collect(Collectors.groupingBy(item -> item.getSubEntity().getId()));

        List<StoreCartResponse> stores = groupedByStore.entrySet().stream()
                .map(entry -> {
                    UUID storeId = entry.getKey();
                    SubEntity store = entry.getValue().get(0).getSubEntity(); // tous appartiennent au même store
                    List<CartItemResponse> itemResponses = entry.getValue().stream().map(item ->


                            new CartItemResponse(
                                    item.getId(),
                                    item.getProduct().getId(),
                                    item.getProduct().getName(),
                                    item.getProduct().getName(),
                                    item.getProduct().getPrice().amount().doubleValue(),
                                    item.getDeal()!=null?item.getDeal().getPrice().amount().doubleValue():
                                            item.getBox().getOffer().getPrice().amount().doubleValue(),
                                    item.getQuantity(),
                                    item.getProduct().getProductImagePath(),
                                    storeId,
                                    true, // selected par défaut
                                    item.getProduct().getDescription(),
                                    item.getCollectionInfo()
                            )
                    ).toList();

                    Double total = itemResponses.stream()
                            .mapToDouble(i -> i.getDiscountPrice() != null ? i.getDiscountPrice() * i.getQuantity() : i.getPrice() * i.getQuantity())
                            .sum();

                    return new StoreCartResponse(
                            storeId,
                            store.getName(),
                            store.getAvatarPath(),
                            total,
                            itemResponses
                    );
                }).toList();

        return new CartResponse(true, new CartData(stores));
    }

    public UpdateQuantityResponse updateItemQuantity(UUID itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        double totalPrice = getItemPrice(item) * item.getQuantity();

        return new UpdateQuantityResponse(
                true,
                "Item quantity updated successfully",
                new UpdateQuantityResponse.Data(
                        item.getId().toString(),
                        item.getQuantity(),
                        totalPrice
                )
        );
    }

    // 3. Remove Single Cart Item
    public RemoveItemResponse removeItem(UUID itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        item.setDeletedAt(Instant.now());
        UUID cartId = item.getCart().getId();
        cartItemRepository.save(item);

        Cart cart = cartRepository.findById(cartId).orElseThrow();
        double updatedTotal = cart.getItems().stream()
                .mapToDouble(i -> getItemPrice(i) * i.getQuantity())
                .sum();

        return new RemoveItemResponse(
                true,
                "Item removed from cart successfully",
                new RemoveItemResponse.Data(itemId.toString(), updatedTotal)
        );
    }

    // 4. Select/Unselect Single Item
    public SelectItemResponse selectItem(UUID itemId, boolean selected) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        // ici tu peux stocker "selected" dans CartItem si tu ajoutes le champ
        // item.setSelected(selected);
        // cartItemRepository.save(item);

        return new SelectItemResponse(
                true,
                "Item selection updated successfully",
                new SelectItemResponse.Data(itemId.toString(), selected)
        );
    }

    public Map<String, Object> selectAllStore(UUID storeId, boolean selected) {
        List<CartItem> items = cartItemRepository.findBySubEntityId(storeId);
        items.forEach(i -> i.setSelected(selected));
        cartItemRepository.saveAll(items);

        return Map.of(
                "success", true,
                "message", "All store items selection updated successfully",
                "data", Map.of(
                        "storeId", storeId,
                        "selectedItemsCount", selected ? items.size() : 0,
                        "totalItems", items.size()
                )
        );
    }

    public Map<String, Object> removeAllStore(UUID storeId) {
        List<CartItem> items = cartItemRepository.findBySubEntityId(storeId);
        cartItemRepository.deleteAll(items);
        return Map.of(
                "success", true,
                "message", "All items removed from store successfully",
                "data", Map.of(
                        "storeId", storeId,
                        "removedItemsCount", items.size()
                )
        );
    }

    public Map<String, Object> clearCart(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow();
        int removedCount = cart.getItems().size();
        cart.getItems().clear();
        cartRepository.save(cart);
        return Map.of(
                "success", true,
                "message", "Cart cleared successfully",
                "data", Map.of(
                        "removedItemsCount", removedCount,
                        "removedStoresCount", 0
                )
        );
    }

    public Map<String, Object> validateCart(List<UUID> selectedItems) {
        List<CartItem> items = cartItemRepository.findAllById(selectedItems);

        List<Map<String, Object>> unavailableItems = new ArrayList<>();
        List<Map<String, Object>> priceChanges = new ArrayList<>();

        double total = 0.0;

        for (CartItem item : items) {
            var product = item.getProduct();

            // ✅ Vérifier si le produit est toujours disponible
            if (product == null || product.getDeletedAt()!=null || item.getQuantity() <= 0) {
                unavailableItems.add(Map.of(
                        "itemId", item.getId(),
                        "reason", "Product unavailable"
                ));
                continue;
            }

            // ✅ Vérifier le prix actuel vs prix stocké dans l’item
            double currentPrice = product.getPrice().amount().doubleValue();
            double expectedPrice = currentPrice; // tu peux stocker old price dans item si nécessaire
            if (item.getQuantity() < 1) {
                unavailableItems.add(Map.of(
                        "itemId", item.getId(),
                        "reason", "Invalid quantity"
                ));
            }

            if (item.getProduct() != null && currentPrice != expectedPrice) {
                priceChanges.add(Map.of(
                        "itemId", item.getId(),
                        "oldPrice", expectedPrice,
                        "newPrice", currentPrice
                ));
            }

            total += currentPrice * item.getQuantity();
        }

        boolean valid = unavailableItems.isEmpty() && priceChanges.isEmpty();

        return Map.of(
                "success", true,
                "data", Map.of(
                        "valid", valid,
                        "unavailableItems", unavailableItems,
                        "priceChanges", priceChanges,
                        "total", total
                )
        );
    }


    public Map<String, Object> getSummary(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow();
        int totalItems = cart.getItems().size();
        int selectedItems = (int) cart.getItems().stream().filter(CartItem::isSelected).count();
        double totalPrice = cart.getItems().stream()
                .mapToDouble(i -> i.getProduct().getPrice().amount().doubleValue() * i.getQuantity())
                .sum();

        return Map.of(
                "success", true,
                "data", Map.of(
                        "totalItems", totalItems,
                        "selectedItems", selectedItems,
                        "totalPrice", totalPrice,
                        "selectedPrice", totalPrice,
                        "storesCount", 1,
                        "deliveryFee", 0.0,
                        "finalTotal", totalPrice
                )
        );
    }

    // Utils
    private double getItemPrice(CartItem item) {
        if (item.getDeal() != null) return item.getDeal().getPrice().amount().doubleValue();
        if (item.getBox() != null) return item.getBox().getOffer().getPrice().amount().doubleValue();
        if (item.getProduct() != null) return item.getProduct().getPrice().amount().doubleValue();
        return 0.0;
    }

}
