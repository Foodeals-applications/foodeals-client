package net.foodeals.order.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.entities.Price;
import net.foodeals.core.domain.enums.TransactionStatus;
import net.foodeals.core.domain.enums.TransactionType;

import java.util.UUID;

public record TransactionRequest(
        @NotBlank String paymentId,
        @NotBlank String reference,
        @NotBlank String context,
        @NotNull Price price,
        @NotNull TransactionStatus status,
        @NotNull TransactionType type,
        @NotNull UUID orderId) {
}
