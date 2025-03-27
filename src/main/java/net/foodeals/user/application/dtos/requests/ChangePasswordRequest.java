package net.foodeals.user.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordRequest (
		@NotBlank String currentPassword,
		@NotBlank String newPassword,
        @NotNull Integer idUser
) {
}

