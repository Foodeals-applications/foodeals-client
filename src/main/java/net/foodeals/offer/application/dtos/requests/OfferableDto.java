package net.foodeals.offer.application.dtos.requests;


import net.foodeals.core.domain.enums.OfferType;

public record OfferableDto(OfferType type, BoxDto box, DealDto deal) {
}
