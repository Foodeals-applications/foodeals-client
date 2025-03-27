package net.foodeals.location.application.dtos.responses;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.valueOjects.Coordinates;

public record CityResponse(UUID id, String name, String code, StateResponse state, List<RegionResponse> regionsResponse,
		Coordinates coordinates) {
}
