package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private String productName ;

    private String productDescription ;

    private String productPhotoUrl;

    private Integer totalProducts;

    private double oldPrice  ;

    private double price ;

    private String subEntityName;

    private String subEntityPhotoUrl;

    private int quantity;

}
