package net.foodeals.location.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.location.domain.enums.AddressType;

import java.util.UUID;

public record AddressRequest(

        @NotBlank String address,

        @NotBlank String extraAddress,

        @NotBlank String zip,

        @NotBlank String contactName,

        @NotBlank String contactEmail,

        @NotBlank String contactPhone,

        @NotNull AddressType addressType,

        @NotNull Coordinates coordinates,


         UUID cityId,  UUID regionId,

        UUID countryId,

        // 👉 IDs provenant de Google Maps (pas de ta DB)
        @NotBlank String cityMapId,

        @NotBlank String regionMapId,

        @NotBlank String countryMapId) {
}
