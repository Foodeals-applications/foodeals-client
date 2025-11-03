package net.foodeals.delivery.application.dtos.responses;

import java.util.List;

import net.foodeals.core.domain.entities.Coordinates;
import net.foodeals.user.application.dtos.responses.UserResponse;

public record DeliveryManPositionsResponse (List<UserResponse>boys,Coordinates positionOrganization,
		Coordinates positionClient){

}
