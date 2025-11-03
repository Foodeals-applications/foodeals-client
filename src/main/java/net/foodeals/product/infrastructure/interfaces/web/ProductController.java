package net.foodeals.product.infrastructure.interfaces.web;

import lombok.RequiredArgsConstructor;
import net.foodeals.common.Utils.DistanceCalculator;
import net.foodeals.core.domain.entities.Deal;
import net.foodeals.core.domain.entities.Product;
import net.foodeals.core.domain.entities.SubEntity;
import net.foodeals.core.domain.entities.User;
import net.foodeals.core.repositories.*;
import net.foodeals.offer.application.dtos.responses.OpenTimeResponse;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.product.application.dtos.responses.PriceResponse;
import net.foodeals.product.application.dtos.responses.ProductDetailsResponse;
import net.foodeals.product.application.dtos.responses.ProductSuggestionResponse;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.user.application.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final SubEntityService subEntityService;
    private final DealRepository dealRepository;
    private final ProductRepository productRepository;
    private final PaymentMethodProductRepository paymentMethodRepository;
    private final DeliveryMethodRepository deliveryMethodRepository;
    private final PickupConditionRepository pickupConditionRepository;
    private final ModelMapper mapper;
    private final UserService userService;


    @GetMapping("/{productId}/details")
    public ResponseEntity<ProductDetailsResponse> getProductDetails(@PathVariable UUID productId) {


        // 1. Récupérer les détails du produit
        Product product = productService.findById(productId);

        //2. Récupérer les détails du magasin lié au produit
        SubEntity subEntity = product.getSubEntity();

        Deal deal = dealRepository.findActiveDealByProduct(productId).get();

        // 3. Récupérer les avis pour le produit
        //ReviewResponse reviews = reviewsService.getProductReviews(productId);

        // 4. Vérifier les modalités et la livraison
        //DeliveryResponse delivery = deliveryService.getDeliveryOptions(productId, store.getId());

        // 5. Obtenir des produits similaires dans la même catégorie
        List<ProductSuggestionResponse> similarProducts = productService.getSimilarProducts(product);

        User connectedUser = userService.getConnectedUser();
        double distance = DistanceCalculator.calculateDistance(connectedUser.getCoordinates().latitude().doubleValue(), connectedUser.getCoordinates().longitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue());

        PriceResponse price = new PriceResponse(deal.getOffer().getPrice().amount().doubleValue(), deal.getOffer().getSalePrice().amount().doubleValue());
        int discountPercentage = (int) Math.round((price.getOldPrice() - price.getNewPrice()) / price.getOldPrice() * 100);

        List<OpenTimeResponse> openTimeResponses = deal.getOffer().getOpenTime().stream()
                .map(openTime -> new OpenTimeResponse(
                        openTime.getId(),
                        openTime.getDate(),
                        openTime.getFrom(),
                        openTime.getTo()
                ))
                .collect(Collectors.toList());
        // 6. Construire la réponse finale
        ProductDetailsResponse response = new ProductDetailsResponse(product.getId(),
                product.getProductImagePath(),
                product.getName(), product.getDescription(),
                deal.getOffer().getModalityTypes(), distance
                , openTimeResponses, price, discountPercentage,
                new ArrayList<>(), product.getStock(), subEntity.getName(), subEntity.getAddress().getAddress() + "" + subEntity.getAddress().getCity().getName()
                , subEntity.getNumberOfLikes(),similarProducts);

        return ResponseEntity.ok(response);
    }


}