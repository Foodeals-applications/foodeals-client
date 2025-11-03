package net.foodeals.delivery.application.dtos.requests;

import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.entities.Coordinates;

import java.util.UUID;

/**
 * DeliveryPositionRequest
 */
public record DeliveryPositionRequest(
        @NotNull Coordinates coordinates,
        @NotNull UUID deliveryId
) {
}
