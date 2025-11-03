package net.foodeals.user.application.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.entities.Name;

import java.util.Date;

public record ClientRegisterRequest(
        @NotNull Name name,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotBlank String password,
        @NotBlank Date birdhdayDate,
        @NotBlank String countryName,
        @NotNull Boolean isEmailVerified) {
}
