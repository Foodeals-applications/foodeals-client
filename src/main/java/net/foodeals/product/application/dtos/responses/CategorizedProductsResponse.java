package net.foodeals.product.application.dtos.responses;

import java.util.List;

public record CategorizedProductsResponse(List<CategorizedProductsDto> categorizedProducts, int totalCategories, long totalProducts) {}
