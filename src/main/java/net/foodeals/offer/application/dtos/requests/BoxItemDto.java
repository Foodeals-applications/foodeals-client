package net.foodeals.offer.application.dtos.requests;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.entities.Price;
import net.foodeals.product.application.dtos.requests.ProductRequest;

public record BoxItemDto(@NotNull ProductRequest productRequest, UUID productId, @NotNull Integer quantity,
                         @NotNull Price price) {
}
