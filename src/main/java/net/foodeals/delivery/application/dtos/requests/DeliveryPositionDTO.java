package net.foodeals.delivery.application.dtos.requests;

import java.util.UUID;

public class DeliveryPositionDTO {
    private UUID deliveryBoyId;
    private Float latitude;
    private Float longitude;

    public DeliveryPositionDTO(UUID deliveryBoyId, Float latitude, Float longitude) {
        this.deliveryBoyId = deliveryBoyId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

	public UUID getDeliveryBoyId() {
		return deliveryBoyId;
	}

	public void setDeliveryBoyId(UUID deliveryBoyId) {
		this.deliveryBoyId = deliveryBoyId;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

    
}

