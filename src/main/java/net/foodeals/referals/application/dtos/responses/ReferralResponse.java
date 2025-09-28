package net.foodeals.referals.application.dtos.responses;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ReferralResponse {
    private UUID id;
    private String email;
    private boolean successful;
    private double reward;
    private Instant createdAt;
}