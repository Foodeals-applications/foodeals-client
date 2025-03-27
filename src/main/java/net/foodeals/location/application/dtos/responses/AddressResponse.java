package net.foodeals.location.application.dtos.responses;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
	private UUID id;
	private String address;
	private String extraAddress;
	private String zip;
	private CityResponse city;
}
