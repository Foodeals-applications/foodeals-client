package net.foodeals.product.application.dtos.requests;


import net.foodeals.core.domain.entities.Price;

public record SupplementDto(String name, Price price, String image) {
}