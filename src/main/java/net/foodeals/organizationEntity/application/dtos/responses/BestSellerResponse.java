package net.foodeals.organizationEntity.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BestSellerResponse {

    private UUID id;
    private String name;
    private String logoUrl;
    private String coverUrl;
    private Double totalSales;
    private Double percentageSales;
    private Double deliveryFee;

}
