package net.foodeals.location.application.dtos.responses;

import net.foodeals.core.domain.entities.Coordinates;

import java.util.UUID;

public record PositionCityResponse(UUID id, Coordinates coordinates) {

}
