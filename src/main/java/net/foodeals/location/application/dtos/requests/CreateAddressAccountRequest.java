package net.foodeals.location.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.core.domain.enums.AddressType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateAddressAccountRequest {

    private AddressType addressType; // Domicile, Travail, Autre
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private String extraAddress;
    private String zip;
    private float latitude;
    private float longitude;
    private UUID cityId;
    private UUID regionId;
    private UUID countryId;
}
