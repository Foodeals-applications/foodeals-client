package net.foodeals.product.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SimilarProductResponse
{
    private UUID id;
    private String name;
    private BigDecimal price;
    private String photoUrl;
}
