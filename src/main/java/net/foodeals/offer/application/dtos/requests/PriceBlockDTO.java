package net.foodeals.offer.application.dtos.requests;


import net.foodeals.core.domain.entities.Price;

public record PriceBlockDTO(Integer quantity, Price price) {
}
