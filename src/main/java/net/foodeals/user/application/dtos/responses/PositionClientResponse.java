package net.foodeals.user.application.dtos.responses;

import net.foodeals.core.domain.entities.Coordinates;

public record PositionClientResponse(Integer id , Coordinates coordinates , Integer raduis) {

}
