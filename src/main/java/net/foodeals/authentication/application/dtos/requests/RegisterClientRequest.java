package net.foodeals.authentication.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.foodeals.user.domain.valueObjects.Name;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record RegisterClientRequest(
        @NotNull Name name,
        @NotBlank String email,
        @NotBlank String phone,
        @NotBlank String password,
        @NotNull LocalDate birthDate,
        @NotNull String countryName,
        Boolean isEmailVerified
) implements AuthRequest {
}