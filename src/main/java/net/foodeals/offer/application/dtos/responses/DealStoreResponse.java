package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DealStoreResponse {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private double discount;
    private String imageUrl;
    private UUID storeId;
    private String storeName;
    private String storeLogoUrl;
    private String validUntil;
    private int quantity;
    private String category;
}