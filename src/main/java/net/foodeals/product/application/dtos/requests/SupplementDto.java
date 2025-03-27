package net.foodeals.product.application.dtos.requests;

import net.foodeals.common.valueOjects.Price;

public record SupplementDto(String name, Price price,String image) {
}