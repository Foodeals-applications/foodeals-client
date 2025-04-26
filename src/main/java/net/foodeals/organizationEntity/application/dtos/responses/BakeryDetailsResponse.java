package net.foodeals.organizationEntity.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.offer.domain.enums.ModalityType;

import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BakeryDetailsResponse {


    private UUID id;
    private String image;
    private String name;
    private String subEntityAddress;
    private List<ModalityType> modalityTypes;
    private double distance;
    private int numberOfLikes;
    private boolean feeDelivered;
    private float numberOfStars;
    private List<DealResponse> deals;
}
