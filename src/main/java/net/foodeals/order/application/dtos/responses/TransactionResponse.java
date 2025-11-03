package net.foodeals.order.application.dtos.responses;


import net.foodeals.core.domain.entities.Price;
import net.foodeals.core.domain.enums.TransactionStatus;
import net.foodeals.core.domain.enums.TransactionType;

import java.util.Date;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        String paymentId,
        String reference,
        String context,
        Price price,
        TransactionStatus status,
        TransactionType type,
        Date createdDate
        ) {
       // OrderResponse order) {
}
