package net.foodeals.delivery.application.dtos.responses;


import net.foodeals.core.domain.entities.Coordinates;
import net.foodeals.core.domain.enums.DeliveryStatus;

public record DeliveryTrackingResponse (Coordinates positionDeliveryMan, Coordinates positionPartener,
                                        Coordinates positionClient, DeliveryStatus status){

}
