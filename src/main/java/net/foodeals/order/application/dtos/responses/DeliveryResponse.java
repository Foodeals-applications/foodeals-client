package net.foodeals.order.application.dtos.responses;

import java.util.UUID;
import net.foodeals.delivery.domain.enums.DeliveryStatus;

public class DeliveryResponse {
	public UUID id;

	public String deliveryPersonName;

	public String deliveryPersonPhoneNumber;

	public DeliveryStatus status;

	public Long duration;

	public DeliveryResponse(UUID id, String deliveryPersonName, String deliveryPersonPhoneNumber, DeliveryStatus status,
			Long duration) {
		super();
		this.id = id;
		this.deliveryPersonName = deliveryPersonName;
		this.deliveryPersonPhoneNumber = deliveryPersonPhoneNumber;
		this.status = status;
		this.duration = duration;
	}

	public DeliveryResponse() {
		super();
	}
	
	

}
