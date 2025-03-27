package net.foodeals.delivery.application.dtos.responses;

import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.delivery.domain.enums.DeliveryStatus;

public record DeliveryTrackingResponse (Coordinates positionDeliveryMan,Coordinates positionPartener,
		Coordinates positionClient,DeliveryStatus status){

}
