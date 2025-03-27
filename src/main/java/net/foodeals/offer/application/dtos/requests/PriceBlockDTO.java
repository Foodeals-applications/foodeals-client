package net.foodeals.offer.application.dtos.requests;

import net.foodeals.common.valueOjects.Price;

public record PriceBlockDTO(Integer quantity, Price price) {
}
