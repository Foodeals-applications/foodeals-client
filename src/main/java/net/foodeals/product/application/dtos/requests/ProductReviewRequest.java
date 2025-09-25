package net.foodeals.product.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewRequest {
    private int rating;   // note 1 à 5
    private String comment;
}