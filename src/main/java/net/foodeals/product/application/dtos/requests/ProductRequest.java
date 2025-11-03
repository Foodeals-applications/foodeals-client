package net.foodeals.product.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.entities.Price;
import net.foodeals.core.domain.enums.ProductType;

import java.util.Date;
import java.util.UUID;

public record ProductRequest(
        @NotBlank String name,
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String barcode,
        @NotNull ProductType type,
        Price price,
        String productImagePath,
        @NotBlank UUID categoryId,
        @NotBlank UUID subCategoryId,
        @NotNull String brand,
        @NotNull String rayon,
        Date expirationDate
) {
}
