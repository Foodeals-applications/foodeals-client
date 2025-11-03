package net.foodeals.user.application.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.entities.Name;
import net.foodeals.core.domain.entities.WorkSchedule;
import net.foodeals.core.domain.enums.Civility;
import net.foodeals.core.domain.enums.Nationality;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

public record UserRequest(
		String avatarPath,
		@Nullable Civility civility,
        @NotNull Name name,
        @NotNull Nationality nationality ,
        @NotNull String cin ,
        @NotNull UUID roleId,
        @NotBlank @Email String email,
        Boolean isEmailVerified,
        @NotBlank String password,
        @NotBlank String phone,
        @Nullable String rayon,
        @Nullable UUID cityId,
        @Nullable String address,
        @Nullable Integer managerId,
        @Nullable List<String> solutionNames,
        @Nullable UUID subEntityId,
        @Nullable UUID organizationEntityId,
        @Nullable List<UUID> coveredZonesIds,
        @Nullable List<WorkSchedule>workSchedules
) {
}