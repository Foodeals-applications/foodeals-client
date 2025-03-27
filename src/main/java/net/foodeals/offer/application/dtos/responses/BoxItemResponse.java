package net.foodeals.offer.application.dtos.responses;

import net.foodeals.common.valueOjects.Price;
import net.foodeals.product.application.dtos.responses.ProductResponse;

public record BoxItemResponse(
        Price price,
        Integer quantity,
        ProductResponse product
) {
}
