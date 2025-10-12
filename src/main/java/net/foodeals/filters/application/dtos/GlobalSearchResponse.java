package net.foodeals.filters.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.organizationEntity.application.dtos.responses.StoreResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityResponse;
import net.foodeals.product.application.dtos.responses.ProductResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalSearchResponse {
    private List<StoreResponse> stores;
    private List<ProductResponse> products;
    private List<DealResponse> deals;
}