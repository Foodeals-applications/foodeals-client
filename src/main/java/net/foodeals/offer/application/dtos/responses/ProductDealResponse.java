package net.foodeals.offer.application.dtos.responses;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.product.domain.enums.ProductType;

public record ProductDealResponse (
		UUID id ,
        String name,
        String title,
        String description,
        String barcode,
        ProductType type,
        Price price,
        String productImagePath,
        UUID categoryId,
        UUID subCategoryId,
        String brand,
        String rayon,
        Date expirationDate
) {

}
