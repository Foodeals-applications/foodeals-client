package net.foodeals.product.application.dtos.responses;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductSuggestionResponse {
    private String id;                 // ID du produit similaire
    private String image;              // URL de l'image du produit
    private String title;              // Titre du produit
    private PriceResponse price;       // Prix (ancien/nouveau)
    private Integer stockAvailable;    // Quantité restante
}
