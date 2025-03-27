package net.foodeals.dlc.application.dtos.requests;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ModificationRequest(@NotNull UUID dlcId,@NotNull Integer userId, @NotNull int previousQuantity,
		@NotNull int modifiedQuantity,
		@NotNull int previousDiscount, @NotNull int modifiedDiscount) {

}
