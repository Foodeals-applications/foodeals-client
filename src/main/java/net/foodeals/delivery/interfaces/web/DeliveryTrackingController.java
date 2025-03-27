package net.foodeals.delivery.interfaces.web;

import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import net.foodeals.delivery.application.dtos.requests.delivery.DeliveryPositionRequest;
import net.foodeals.delivery.application.dtos.responses.DeliveryTrackingResponse;
import net.foodeals.delivery.domain.entities.Delivery;
import net.foodeals.delivery.domain.entities.DeliveryPosition;
import net.foodeals.delivery.domain.repositories.DeliveryRepository;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

@Controller
@RequiredArgsConstructor
public class DeliveryTrackingController {

	private final UserService userService;
	private final OrderService orderService;
	private final DeliveryRepository repository;

	@MessageMapping("/updatePosition")
	@SendTo("/topic/position/{idOrder}/{idDeliveryBoy}")
	public DeliveryTrackingResponse updatePosition(@PathVariable UUID idOrder, @PathVariable Integer idDeliveryBoy,
			@RequestBody DeliveryPositionRequest positionRequest) {

		Order order = orderService.findById(idOrder);

		if (order == null) {
			return null;
		}

		User deliveryBoy = userService.findById(idDeliveryBoy);
		if (deliveryBoy == null) {
			return null;
		}
		DeliveryPosition position = new DeliveryPosition();
		position.setCoordinates(positionRequest.coordinates());
		Delivery delivery = order.getDelivery();
		delivery.setDeliveryPosition(position);

		delivery = repository.save(delivery);

		deliveryBoy.setCoordinates(positionRequest.coordinates());

		OrganizationEntity partner = userService.getConnectedUser().getOrganizationEntity();
		DeliveryTrackingResponse deliveryResponse = new DeliveryTrackingResponse(deliveryBoy.getCoordinates(),
				partner.getAddress().getCoordinates(), order.getClient().getAddress().getCoordinates(),
				delivery.getStatus());
		return deliveryResponse;
	}
}