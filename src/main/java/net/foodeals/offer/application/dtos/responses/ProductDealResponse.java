package net.foodeals.offer.application.dtos.responses;

import net.foodeals.core.domain.entities.Price;
import net.foodeals.core.domain.enums.ProductType;

import java.util.Date;
import java.util.UUID;


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
