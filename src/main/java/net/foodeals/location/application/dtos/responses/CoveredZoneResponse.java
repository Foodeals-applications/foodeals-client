package net.foodeals.location.application.dtos.responses;

import java.util.UUID;

public record CoveredZoneResponse(UUID id, RegionResponse regionResponse) {
}
