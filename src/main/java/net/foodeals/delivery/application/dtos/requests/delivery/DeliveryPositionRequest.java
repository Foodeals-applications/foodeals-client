package net.foodeals.delivery.application.dtos.requests.delivery;

import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.entities.Coordinates;


/**
 * DeliveryPositionRequest
 */
public record DeliveryPositionRequest(
        @NotNull Coordinates coordinates) {
}
