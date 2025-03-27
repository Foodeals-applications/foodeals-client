package net.foodeals.order.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record CancelRequest(
		@NotBlank String reason,
		@NotBlank String subject) {
}
