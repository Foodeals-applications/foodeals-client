package net.foodeals.delivery.application.services;

import net.foodeals.common.contracts.UseCase;
import net.foodeals.core.domain.entities.DeliveryPosition;
import net.foodeals.delivery.application.dtos.requests.DeliveryPositionRequest;

public interface AddNewDeliveryPositionToDelivery extends UseCase<DeliveryPositionRequest, DeliveryPosition> {
}
    
