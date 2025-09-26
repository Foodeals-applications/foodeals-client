package net.foodeals.user.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.foodeals.product.application.dtos.responses.ProductResponse;

import java.util.List;

@Data
@AllArgsConstructor
public class FavoritesResponse {
    private List<PartnerResponse> partners;
    private List<ProductResponse> products;
}