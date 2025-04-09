package net.foodeals.product.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PriceResponse {
    private Double oldPrice;           // Prix avant réduction
    private Double newPrice;           // Prix après réduction
}
