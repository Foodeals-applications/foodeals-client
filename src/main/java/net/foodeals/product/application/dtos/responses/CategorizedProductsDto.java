package net.foodeals.product.application.dtos.responses;

import java.util.List;
import java.util.UUID;

public record CategorizedProductsDto(String categoryName, UUID categoryId, String categoryImage, List<ProductSearchedDto> products) {}