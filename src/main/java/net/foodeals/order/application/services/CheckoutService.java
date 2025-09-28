package net.foodeals.order.application.services;

import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.responses.CartItemResponse;
import net.foodeals.offer.domain.entities.CartItem;
import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.offer.domain.repositories.CartRepository;
import net.foodeals.order.application.dtos.responses.CheckoutDataResponse;
import net.foodeals.order.application.dtos.responses.DeliveryOptionResponse;
import net.foodeals.order.domain.repositories.DeliveryOptionRepository;
import net.foodeals.product.application.dtos.responses.PaymentMethodResponse;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.repositories.PaymentMethodRepository;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final UserService userService;
    private final CartRepository cartRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final DeliveryOptionRepository deliveryOptionRepository;

    public CheckoutDataResponse getCheckoutData() {
        User user = userService.getConnectedUser();
        // 1️⃣ Récupérer les items du panier
        List<CartItemResponse> items = cartRepository.findByUserId(user.getId()).get().getItems().stream()
                .map(item -> toCartItemResponse(item)
                )
                .toList();

        // 2️⃣ Récupérer les méthodes de paiement
        List<PaymentMethodResponse> paymentMethods = paymentMethodRepository.findAll().stream()
                .map(pm -> new PaymentMethodResponse(pm.getId(), pm.getMethodName()))
                .toList();

        // 3️⃣ Récupérer les options de livraison
        List<DeliveryOptionResponse> deliveryOptions = deliveryOptionRepository.findAll().stream()
                .map(opt -> new DeliveryOptionResponse(
                        "TYPE",
                        opt.getLabel(),
                        opt.getCost(),
                        opt.getCurrency(),
                        opt.getEstimatedTime()
                ))
                .toList();

        return new CheckoutDataResponse(items, paymentMethods, deliveryOptions);
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