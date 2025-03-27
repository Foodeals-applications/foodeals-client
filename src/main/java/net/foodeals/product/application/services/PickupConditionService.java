package net.foodeals.product.application.services;

import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.product.application.dtos.requests.PickupConditionRequest;
import net.foodeals.product.application.dtos.responses.PickupConditionResponse;

public interface PickupConditionService extends CrudService<PickupConditionResponse, UUID, PickupConditionRequest> {

}
