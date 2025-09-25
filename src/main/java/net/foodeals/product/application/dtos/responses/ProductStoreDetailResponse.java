package net.foodeals.product.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStoreDetailResponse {
    private ProductStoreResponse product;
    private List<ProductStoreResponse> relatedProducts;
}
