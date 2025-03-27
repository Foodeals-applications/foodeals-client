package net.foodeals.location.application.dtos.responses;

import java.util.UUID;

import net.foodeals.common.valueOjects.Coordinates;

public record PositionCityResponse(UUID id,Coordinates coordinates) {

}
