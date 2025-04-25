package net.foodeals.organizationEntity.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.offer.domain.enums.ModalityType;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class HotelDetailsResponse {

    private UUID id;

    private String hotelName;

    private String hotelAddress;

    private String hotelPhoto;
    private List<ModalityType> modalityTypes;
    private double distance;
    private int numberOfLikes;
    private boolean feeDelivered;
    private float numberOfStars;

}
