package net.foodeals.product.application.dtos.responses;


import java.util.UUID;

public record ProductSubCategoryResponse(
        UUID id,
        String name,
        String slug
) {
}
