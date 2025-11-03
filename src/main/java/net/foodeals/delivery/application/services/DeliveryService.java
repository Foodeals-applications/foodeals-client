package net.foodeals.delivery.application.services;

import java.time.Instant;
import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.Delivery;
import net.foodeals.delivery.application.dtos.requests.delivery.DeliveryRequest;

/**
 * DeliveryService
 */
public interface DeliveryService extends CrudService<Delivery, UUID, DeliveryRequest> {
	Long countDeliveriesByDeliveryPartner(UUID organizationEntityId);

	public long getTotalOrders(Integer deliveryBoyId,Instant startDate, Instant endDate);

	public long getOrdersInProgress(Integer deliveryBoyId,Instant startDate, Instant endDate);

	public long getScheduledOrders(Integer deliveryBoyId,Instant startDate, Instant endDate);

	public long getDeliveredOrders(Integer deliveryBoyId,Instant startDate, Instant endDate);

	public long getActiveDeliveryBoys(UUID organizationId);

	public long getInactiveDeliveryBoys(UUID organizationId);
}
