package net.foodeals.offer.application.dtos.responses;

import net.foodeals.common.valueOjects.Price;
import net.foodeals.offer.domain.enums.OfferType;
import net.foodeals.offer.domain.valueObject.Offerable;
import net.foodeals.offer.infrastructure.modelMapperConfig.BoxResponse;

import java.util.List;
import java.util.UUID;

public record OfferResponse(UUID id, Price price, Price salePrice, Integer reduction, String barcode,
		List<OpenTimeResponse> openTime, Offerable offerable, OfferType type,
		DealResponse deal , BoxResponse box 
) {
}