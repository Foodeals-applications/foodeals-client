package net.foodeals.product.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.foodeals.order.application.dtos.responses.DeliveryResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProductDetailsResponse {

    private SubEntityResponse store;                     // Détails sur le magasin
    private ProductResponse product;                // Détails sur le produit
    private ReviewResponse reviews;                 // Nombre d'avis (ou détails des avis)
    private DeliveryResponse delivery;              // Options de livraison et retrait
    private List<ProductSuggestionResponse> similarProducts; // Produits similaires
}