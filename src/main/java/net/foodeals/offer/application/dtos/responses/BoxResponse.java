package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.product.application.dtos.responses.ProductOfferResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoxResponse {
    private String id;
    private String title;
    private String description;
    private double price;
    private String imageUrl;
    private String storeId;
    private String storeName;
    private String storeLogoUrl;
    private List<ProductOfferResponse> items;
    private String availableUntil;
    private String category;
}

