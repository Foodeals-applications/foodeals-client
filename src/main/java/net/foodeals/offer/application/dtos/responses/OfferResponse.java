package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferResponse {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private double discount;
    private String imageUrl;
    private String storeId;
    private String storeName;
    private String storeLogoUrl;
    private String validUntil;
    private String type; // flash, daily, etc.
    private double distance; // calculé à partir de location
}
