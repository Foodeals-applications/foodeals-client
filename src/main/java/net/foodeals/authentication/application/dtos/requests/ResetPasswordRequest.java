package net.foodeals.authentication.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank String token,
        @NotBlank @Size(min = 8, message = "Password must contain at least 8 characters") String newPassword
) {
}
