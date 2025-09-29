package net.foodeals.order.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessPaymentResponse {
    private UUID transactionId;
    private String status; // SUCCESS, FAILED, PENDING
    private String message;
}