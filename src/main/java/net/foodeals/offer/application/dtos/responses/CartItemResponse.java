package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private UUID id;
    private UUID productId;
    private String name;
    private String productName;
    private Double price;
    private Double discountPrice;
    private Integer quantity;
    private String imageUrl;
    private UUID storeId;
    private boolean selected;
    private String description;
    private String collectionInfo;

}
