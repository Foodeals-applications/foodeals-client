package net.foodeals.product.application.services;

import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.product.application.dtos.requests.DeliveryMethodRequest;
import net.foodeals.product.application.dtos.responses.DeliveryMethodResponse;

public interface DeliveryMethodService extends CrudService<DeliveryMethodResponse, UUID, DeliveryMethodRequest> {

}
