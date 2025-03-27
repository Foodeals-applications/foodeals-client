package net.foodeals.organizationEntity.application.dtos.requests;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record SubEntityRequest

(@NotNull String name, @NotNull List<String> activiteNames, @NotNull String avatarPath, @NotNull String coverPath,
		@NotNull String email, @NotNull String phone, @NotNull List<String> solutionNames,
		@NotNull Integer managerId, @NotNull UUID cityId, @NotNull UUID regionId,
		@NotNull UUID countryId, @NotNull String exactAdresse, @NotNull String iFrame) {

}
