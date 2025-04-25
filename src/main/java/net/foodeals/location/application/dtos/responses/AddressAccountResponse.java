package net.foodeals.location.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.location.domain.enums.AddressType;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressAccountResponse {

    private AddressType addressType; // Domicile, Travail, Autre
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private String extraAddress;
    private String zip;
    private Float latitude;
    private Float longitude;
    private String city;
    private String region;
    private String country;
}
