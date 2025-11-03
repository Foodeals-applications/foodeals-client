package net.foodeals.product.application.dtos.responses;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.foodeals.core.domain.entities.Product;

@Data
@AllArgsConstructor
@Builder
public class ProductResponse {
	private UUID id ;
	private String image;
	private String name;
	private String description;
	private PriceResponse price ;
	private List<String>categories ;
	private Integer stock;
	private UUID subEntityId ;

    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductImagePath(),   // image
                product.getName(),
                product.getDescription(),
                new PriceResponse(
                        product.getPrice().amount().doubleValue(),
                        product.getPrice().amount().doubleValue()
                ),
                product.getCategory() != null
                        ? List.of(product.getCategory().getName())
                        : List.of(),
                product.getStock(),
                product.getSubEntity() != null ? product.getSubEntity().getId() : null
        );
    }
}

