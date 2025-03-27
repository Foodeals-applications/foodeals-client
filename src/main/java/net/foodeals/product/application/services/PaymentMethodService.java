package net.foodeals.product.application.services;

import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.product.application.dtos.requests.PaymentMethodRequest;
import net.foodeals.product.application.dtos.responses.PaymentMethodResponse;

public interface PaymentMethodService extends CrudService<PaymentMethodResponse, UUID, 
PaymentMethodRequest> {

}
