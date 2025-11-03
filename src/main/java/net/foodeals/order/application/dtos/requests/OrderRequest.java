package net.foodeals.order.application.dtos.requests;

import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.entities.Price;
import net.foodeals.core.domain.enums.OrderStatus;
import net.foodeals.core.domain.enums.OrderType;
import net.foodeals.delivery.application.dtos.requests.delivery.DeliveryRequest;
import net.foodeals.location.application.dtos.requests.AddressRequest;

import java.util.UUID;

public record OrderRequest(
        @NotNull Price price,
        @NotNull OrderType type,
        @NotNull Integer quantity,
        @NotNull OrderStatus status,
        @NotNull Integer clientId,
        @NotNull UUID offerId,
        UUID couponId,
        AddressRequest shippingAddress,
        DeliveryRequest delivery
) {
}
