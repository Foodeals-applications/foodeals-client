package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.product.application.dtos.responses.ProductOfferResponse;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoxFeaturedResponse {
    private UUID id;
    private String name;
    private String description;
    private Double salePrice;
    private Double originalPrice;
    private String subEntityName;
}

