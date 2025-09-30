package net.foodeals.location.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressesResponse {
    private AddressResponse mainAddress;
    private List<AddressResponse> otherAddresses;
}