package net.foodeals.product.application.dtos.responses;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductSuggestionResponse {
    private UUID id;                 // ID du produit similaire
    private String image;              // URL de l'image du produit
    private String title;              // Titre du produit
    private PriceResponse price;       // Prix (ancien/nouveau)
    private Integer stockAvailable;    // Quantité restante
}
