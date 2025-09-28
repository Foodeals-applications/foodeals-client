package net.foodeals.offer.application.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class DriverDto {
    private String id;
    private String name;
    private String phone;
}