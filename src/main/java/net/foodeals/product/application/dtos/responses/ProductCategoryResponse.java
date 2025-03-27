package net.foodeals.product.application.dtos.responses;

import java.util.List;
import java.util.UUID;

public record ProductCategoryResponse(UUID id, String name, String slug, List<ProductSubCategoryResponse> subCategories) {
}
