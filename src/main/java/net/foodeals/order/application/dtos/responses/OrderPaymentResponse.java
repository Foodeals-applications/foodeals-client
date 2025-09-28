package net.foodeals.order.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentResponse {
    private UUID id;
    private String status;
    private Integer quantity;
    private String offerTitle;
    private String customerName;
    private Instant createdAt;
}