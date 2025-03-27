package net.foodeals.product.application.dtos.responses;

import java.util.UUID;

public record PickupConditionResponse(UUID id, String conditionName, int daysBeforePickup,
		 int discountPercentage) {
}
