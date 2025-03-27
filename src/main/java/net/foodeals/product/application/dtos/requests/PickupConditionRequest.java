package net.foodeals.product.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record PickupConditionRequest(@NotBlank String conditionName, @NotBlank int daysBeforePickup,
		@NotBlank int discountPercentage) {

}
