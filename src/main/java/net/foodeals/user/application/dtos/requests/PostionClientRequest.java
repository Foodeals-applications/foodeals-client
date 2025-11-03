package net.foodeals.user.application.dtos.requests;

import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.entities.Coordinates;

public record PostionClientRequest(Integer id , Coordinates coordinates, @NotNull Integer raduis) {

}
