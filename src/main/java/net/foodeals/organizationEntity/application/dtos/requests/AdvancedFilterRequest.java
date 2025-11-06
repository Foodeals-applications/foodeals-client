package net.foodeals.organizationEntity.application.dtos.requests;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record AdvancedFilterRequest(
        String searchQuery,
        List<UUID> categoryIds,
        UUID domainId,
        PriceRange priceRange,
        Filters filters,
        SortBy sort,
        Pagination pagination
) {
    public record PriceRange(BigDecimal min, BigDecimal max) {}
    public record Filters(Boolean onlyAvailable, Boolean onlyDiscounted, Double minRating) {}
    public record SortBy(String field, String order) {}
    public record Pagination(Integer limit, Integer offset) {}
}