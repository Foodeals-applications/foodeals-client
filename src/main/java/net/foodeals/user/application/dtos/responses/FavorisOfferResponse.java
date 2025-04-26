package net.foodeals.user.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.valueOjects.Price;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FavorisOfferResponse {

    private UUID dealId ;

    private String photoProduct;

    private String nameProduct;

    private Price oldPrice;

    private Price newPrice;

    private Integer percentageReduction;

    private boolean feeDelivered;
}
