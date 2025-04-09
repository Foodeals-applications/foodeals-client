package net.foodeals.product.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReviewResponse {
    private Integer count;             // Nombre total de retours/avis
}
