package net.foodeals.product.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewResponse {
    private UUID id;
    private int rating;
    private String comment;
    private UUID productId;
    private Instant createdAt;
}