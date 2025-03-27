package net.foodeals.user.application.dtos.requests;

import jakarta.validation.constraints.NotNull;
import net.foodeals.common.valueOjects.Coordinates;

public record PostionClientRequest(Integer id ,Coordinates coordinates, @NotNull Integer raduis) {

}
