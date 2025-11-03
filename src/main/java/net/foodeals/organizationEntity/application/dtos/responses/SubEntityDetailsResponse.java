package net.foodeals.organizationEntity.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.foodeals.core.domain.entities.Coordinates;
import net.foodeals.product.application.dtos.responses.CategoryProductsResponse;
import net.foodeals.product.application.dtos.responses.ProductOfferResponse;

import java.util.List;

@Data
@AllArgsConstructor
public class SubEntityDetailsResponse {
    private String name;
    private String logo;
    private String cover;
    private Integer totalSales;
    private String address;
    private String type;
    private List<String> categoriesWithOffers;
    private List<ProductOfferResponse> productsOnOffer;
    private List<CategoryProductsResponse> categorizedProducts;
    private Integer numberOfLikes;
    private boolean deliveryFree;
    private boolean isFavorite;
    private Coordinates location ;

}