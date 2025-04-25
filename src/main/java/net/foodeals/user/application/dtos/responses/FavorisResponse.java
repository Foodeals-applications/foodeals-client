package net.foodeals.user.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.valueOjects.Price;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FavorisResponse {

    private String photoProduct;

    private String nameProduct;

    private Price oldPrice;

    private Price newPrice;

    private Integer percentageReduction;

    private boolean feeDelivered;
}
