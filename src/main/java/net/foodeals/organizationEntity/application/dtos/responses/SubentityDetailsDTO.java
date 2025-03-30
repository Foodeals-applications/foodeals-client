package net.foodeals.organizationEntity.application.dtos.responses;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.foodeals.product.application.dtos.responses.CategoryWithOffersDTO;
import net.foodeals.product.application.dtos.responses.ProductCategoryDTO;
import net.foodeals.product.application.dtos.responses.ProductWithOfferDTO;

@Setter
@Getter
public class SubentityDetailsDTO {
    private String name;
    private String logo;
    private String cover;
    private BigDecimal totalSales;
    private String address;
    private String type;
    private int numberOfLikes;
    private boolean deliveryFree;
    private List<CategoryWithOffersDTO> categoriesWithOffers;
    private List<ProductWithOfferDTO> productsWithPriceOffers;
    private List<ProductCategoryDTO> productsByCategory;

    // Getters and Setters
}
