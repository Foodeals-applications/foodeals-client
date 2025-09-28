package net.foodeals.businessrecomendations.application.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class BusinessRegisterResponse {
    private UUID id;
    private String businessName;
    private String status;
    private Instant submittedAt;
    private String message;
}