package net.foodeals.order.application.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDto {
    private double latitude;
    private double longitude;
}