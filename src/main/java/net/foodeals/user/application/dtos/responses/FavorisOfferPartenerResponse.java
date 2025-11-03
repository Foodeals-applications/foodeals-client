package net.foodeals.user.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.core.domain.entities.Price;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FavorisOfferPartenerResponse {

    private UUID dealId ;

    private String namePartner;

    private String photoPartner;

    private String photoProduct;

    private String nameProduct;

    private Price oldPrice;

    private Price newPrice;

    private Integer percentageReduction;

    private boolean feeDelivered;
}
