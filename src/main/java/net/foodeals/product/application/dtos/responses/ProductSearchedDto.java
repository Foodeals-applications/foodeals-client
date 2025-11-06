package net.foodeals.product.application.dtos.responses;


import java.math.BigDecimal;
import java.util.UUID;

public record ProductSearchedDto(
        UUID id,
        String name,
        String description,
        String image,
        BigDecimal newPrice,
        BigDecimal oldPrice,
        Integer stock,
        UUID categoryId,
        String categoryName,
        Boolean available,
        Integer discount,
        Double rating,
        Integer numberOfOrders
) {}
