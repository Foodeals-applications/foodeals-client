package net.foodeals.product.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.foodeals.offer.application.dtos.responses.OpenTimeResponse;
import net.foodeals.offer.domain.entities.OpenTime;
import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.order.application.dtos.responses.DeliveryResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductDetailsResponse {

    private UUID id ;
    private String image;
    private String name;
    private String description;
    private List<ModalityType> modalityTypes;
    private double distance ;
    private List<OpenTimeResponse> openTimes;
    private PriceResponse price ;
    private int discountPercentage;
    private List<String>categories ;
    private Integer stock;
    private String subEntityName ;
    private String subEntityAddress;
    private float noteOfSubentity;
    private List<ProductSuggestionResponse> similarProducts=new ArrayList<>();
    
}