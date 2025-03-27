package net.foodeals.user.application.dtos.responses;

import net.foodeals.common.valueOjects.Coordinates;

public record PositionClientResponse(Integer id ,Coordinates coordinates , Integer raduis) {

}
