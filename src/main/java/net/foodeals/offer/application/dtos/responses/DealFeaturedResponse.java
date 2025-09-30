package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DealFeaturedResponse {

    private UUID id;
    private String name;
    private String description;
    private Double salePrice;
    private Double originalPrice;
    private String subEntityName;
    private String subEntityLogo;
}
