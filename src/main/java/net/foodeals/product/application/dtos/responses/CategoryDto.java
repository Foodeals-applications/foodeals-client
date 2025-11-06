package net.foodeals.product.application.dtos.responses;

import java.util.UUID;

public record CategoryDto(UUID id, String name, String description, String image, long itemsCount, String icon, Integer order) {}