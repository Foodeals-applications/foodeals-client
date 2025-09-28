package net.foodeals.offer.application.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
class TrackingStepDto {
    private String status;
    private String description;
    private Instant timestamp;
}
