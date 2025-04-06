package net.foodeals.product.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryProductsResponse {
    private String categoryName;
    private List<ProductResponse> products;
}
