package net.foodeals.product.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductOfferResponse {
    private UUID id ;
    private String image;
    private String name;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private Integer stock;
}

