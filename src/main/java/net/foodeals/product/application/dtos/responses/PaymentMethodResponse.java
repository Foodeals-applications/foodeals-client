package net.foodeals.product.application.dtos.responses;

import java.util.UUID;

public record PaymentMethodResponse(UUID id, String methodName) {
}
