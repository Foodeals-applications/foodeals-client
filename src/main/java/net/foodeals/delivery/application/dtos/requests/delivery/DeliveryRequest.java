package net.foodeals.delivery.application.dtos.requests.delivery;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.enums.DeliveryStatus;

/**
 * DeliveryRequest
 */
public record DeliveryRequest(
        @NotNull Integer deliveryBoyId,
        @NotNull DeliveryStatus status,
        @NotNull UUID orderId,
        @NotNull DeliveryPositionRequest initialPosition) {
}
