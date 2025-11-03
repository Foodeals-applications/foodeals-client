package net.foodeals.organizationEntity.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.core.domain.entities.Coordinates;
import net.foodeals.core.domain.enums.ModalityType;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.product.application.dtos.responses.CategoryProductsResponse;
import net.foodeals.product.application.dtos.responses.ProductOfferResponse;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RestaurantDetailsResponse {

    private UUID id;
    private String name;
    private String logo;
    private String cover;
    private Integer totalSales;
    private String subEntityAddress;
    private List<ModalityType> modalityTypes;
    private double distance;
    private int numberOfLikes;
    private boolean feeDelivered;
    private float numberOfStars;
    private List<DealResponse>deals;
    private List<String> categoriesWithOffers;
    private List<ProductOfferResponse> productsOnOffer;
    private List<CategoryProductsResponse> categorizedProducts;
    private boolean isFavorite;
    private Coordinates location ;

}
