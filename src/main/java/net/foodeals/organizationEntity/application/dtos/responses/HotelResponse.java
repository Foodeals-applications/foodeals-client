package net.foodeals.organizationEntity.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class HotelResponse {

    private UUID id;
    private String logo;
    private String cover;
    private String name;
    private double distance;
    private int numberOfLikes;
    private float numberOfStars;

}
