package net.foodeals.organizationEntity.application.dtos.responses;

import net.foodeals.product.application.dtos.responses.ProductSearchedDto;

import java.util.List;

public record SearchResponse(List<ProductSearchedDto> results, long total, int limit, int offset) {}