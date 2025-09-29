package net.foodeals.order.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessPaymentRequest {
    private UUID orderId;
    private String paymentMethodId;
    private Double amount;
}
