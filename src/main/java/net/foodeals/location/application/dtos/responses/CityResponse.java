package net.foodeals.location.application.dtos.responses;

import net.foodeals.core.domain.entities.Coordinates;

import java.util.List;
import java.util.UUID;


public record CityResponse(UUID id, String name, String code, StateResponse state, List<RegionResponse> regionsResponse,
		Coordinates coordinates) {
}
