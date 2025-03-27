package net.foodeals.delivery.application.services.impl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.Utils.DistanceCalculator;
import net.foodeals.delivery.application.dtos.requests.DeliveryPositionRequest;
import net.foodeals.delivery.application.dtos.requests.delivery.DeliveryRequest;
import net.foodeals.delivery.application.services.AddNewDeliveryPositionToDelivery;
import net.foodeals.delivery.application.services.DeliveryService;
import net.foodeals.delivery.domain.entities.Delivery;
import net.foodeals.delivery.domain.entities.DeliveryPosition;
import net.foodeals.delivery.domain.enums.DeliveryStatus;
import net.foodeals.delivery.domain.exceptions.DeliveryNotFoundException;
import net.foodeals.delivery.domain.repositories.DeliveryRepository;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

/**
 * DeliveryServiceImpl
 */
@Service
@Transactional
@RequiredArgsConstructor
class DeliveryServiceImpl implements DeliveryService {

	private final DeliveryRepository repository;
	private final AddNewDeliveryPositionToDelivery addNewDeliveryPositionToDelivery;
	private final UserService userService;
	private final OrderRepository orderRepository;
	private final ModelMapper mapper;

	@Override
	public List<Delivery> findAll() {
		return repository.findAll();
	}

	@Override
	public Page<Delivery> findAll(Integer pageNumber, Integer pageSize) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public Delivery findById(UUID id) {
		return repository.findById(id).orElseThrow(() -> new DeliveryNotFoundException(id));
	}

	@Override
	public Delivery create(DeliveryRequest request) {
		final User deliveryBoy = userService.findById(request.deliveryBoyId());
		final Delivery delivery = Delivery.create(deliveryBoy, request.status());
		final Delivery savedDelivery = repository.save(delivery);

		DeliveryPosition deliveryPosition = addNewDeliveryPositionToDelivery
				.execute(new DeliveryPositionRequest(request.initialPosition().coordinates(), savedDelivery.getId()));
		savedDelivery.setDeliveryPosition(deliveryPosition);
		Order order = orderRepository.findById(request.orderId()).get();
		order.setDelivery(savedDelivery);
		order = order.setDelivery(savedDelivery);
		order = orderRepository.save(order);
		savedDelivery.setOrder(order);
		Long duration = calculateEstimatedDeliveryDuration(savedDelivery, order.getShippingAddress());
		savedDelivery.setDuration(duration);
		return repository.save(savedDelivery);
	}

	@Override
	public Delivery update(UUID id, DeliveryRequest dto) {
		final User deliveryBoy = userService.findById(dto.deliveryBoyId());
		final Delivery delivery = findById(id);
		mapper.map(dto, delivery);
		delivery.setDeliveryBoy(deliveryBoy);
		// TODO assign orders
		return repository.save(delivery);
	}

	@Override
	public void delete(UUID id) {
		if (!repository.existsById(id))
			throw new DeliveryNotFoundException(id);
		repository.softDelete(id);
	}

	@Override
	public Long countDeliveriesByDeliveryPartner(UUID organizationEntityId) {
		return this.repository.countDeliveriesByDeliveryPartner(organizationEntityId);
	}

	private Long calculateEstimatedDeliveryDuration(Delivery delivery, Address shippingAddress) {
		if (delivery == null || shippingAddress == null) {
			return null;
		}

		if (shippingAddress.getCoordinates().latitude() != null && shippingAddress.getCoordinates().longitude() != null
				&& delivery.getDeliveryPosition().getCoordinates().latitude() != null
				&& delivery.getDeliveryPosition().getCoordinates().longitude() != null) {

			double lat1 = shippingAddress.getCoordinates().latitude();
			double lon1 = shippingAddress.getCoordinates().longitude();
			double lat2 = delivery.getDeliveryPosition().getCoordinates().latitude();
			double lon2 = delivery.getDeliveryPosition().getCoordinates().longitude();

			double distanceInKm = DistanceCalculator.calculateDistance(lat1, lon1, lat2, lon2);

			double speedKmPerHour = 40.0;
			return (long) ((distanceInKm / speedKmPerHour) * 60);
		}

		return null;
	}

	public long getTotalOrders(Integer deliveryBoyId,Instant startDate, Instant endDate) {
		return repository.countTotalDeliveriesByDeliveryBoy(deliveryBoyId,startDate,endDate);
	}

	
	public long getOrdersInProgress(Integer deliveryBoyId,Instant startDate, Instant endDate) {
		return repository.countInProgressDeliveriesByDeliveryBoy(deliveryBoyId, startDate, endDate);
	}

	public long getScheduledOrders(Integer deliveryBoyId,Instant startDate, Instant endDate) {
		return repository.countScheduledDeliveriesByDeliveryBoy(deliveryBoyId, startDate, endDate);
	}

	public long getDeliveredOrders(Integer deliveryBoyId,Instant startDate, Instant endDate) {
		return repository.countDeliveredDeliveriesByDeliveryBoy(deliveryBoyId, startDate, endDate);
	}


	public long getActiveDeliveryBoys(UUID organizationId) {
		return repository.countActiveDeliveryBoys(organizationId);
	}

	public long getInactiveDeliveryBoys(UUID organizationId) {
		return repository.countInactiveDeliveryBoys(organizationId);
	}

}
