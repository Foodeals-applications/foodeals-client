package net.foodeals.offer.application.dtos.requests;

import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.entities.Price;

import java.util.UUID;

public record OfferRequest(
        @NotNull Price price,
        @NotNull Price salePrice,
        @NotNull Integer reduction,
        @NotNull String barcode,
        @NotNull UUID activityId,
        OpenTimeDto openTime,
        OfferableDto offerable
) {
}
