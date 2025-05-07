package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class SimilarDealResponse {

    private UUID id ;

    private String title ;

    private String photo ;

    private BigDecimal newPrice ;

    private BigDecimal oldPrice ;

}
