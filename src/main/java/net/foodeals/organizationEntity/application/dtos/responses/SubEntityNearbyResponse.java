package net.foodeals.organizationEntity.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubEntityNearbyResponse {
    private UUID id;
    private String name;
    private String photo ;
    private String address;
    private Double distanceKm;
    private Double rating;
}