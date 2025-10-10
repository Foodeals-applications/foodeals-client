package net.foodeals.location.application.dtos.responses;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.location.domain.enums.AddressType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    UUID id;
    AddressType addressType;
    String contactName;
    String contactEmail;
    String contactPhone;
    String address;
    String extraAddress;
    String zip;
    Coordinates coordinates;
    String cityName;
    String regionName;
    String countryName;
    String idMapCity;
    String idMapRegion;
    String idMapCountry;
}
