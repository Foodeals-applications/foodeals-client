package net.foodeals.location.infrastructure.modelMapperConfig;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.*;
import net.foodeals.location.application.dtos.responses.AddressResponse;
import net.foodeals.location.application.dtos.responses.CityResponse;
import net.foodeals.location.application.dtos.responses.CountryResponse;
import net.foodeals.location.application.dtos.responses.CoveredZoneResponse;
import net.foodeals.location.application.dtos.responses.RegionResponse;
import net.foodeals.location.application.dtos.responses.StateResponse;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Transactional
public class LocationModelMapperConfig {

	private final ModelMapper mapper;

	@PostConstruct
	public void configure() {
		mapper.addConverter(context -> {
			final Country country = context.getSource();
			return new CountryResponse(country.getId(), country.getName());
		}, Country.class, CountryResponse.class);

		mapper.addConverter(context -> {
			final State state = context.getSource();
			final CountryResponse countryResponse = mapper.map(state.getCountry(), CountryResponse.class);
			return new StateResponse(state.getId(), state.getName(), state.getCode(), countryResponse);
		}, State.class, StateResponse.class);

		mapper.addConverter(context -> {
			final Region region = context.getSource();

			return new RegionResponse(region.getId(), region.getName());
		}, Region.class, RegionResponse.class);

		mapper.addConverter(context -> {
			final City city = context.getSource();
			List<RegionResponse> regionResponses = city.getRegions().stream()
					.map(region -> mapper.map(region, RegionResponse.class)).toList();
			final StateResponse stateResponse = mapper.map(city.getState(), StateResponse.class);
			return new CityResponse(city.getId(), city.getName(), city.getCode(), stateResponse, regionResponses,
					city.getCoordinates());
		}, City.class, CityResponse.class);

		
		mapper.addConverter(context -> {
			final CoveredZones coveredZones = context.getSource();
			RegionResponse regionResponse=mapper.map(coveredZones.getRegion(), RegionResponse.class);
			return new CoveredZoneResponse(coveredZones.getId(),regionResponse);
		}, CoveredZones.class, CoveredZoneResponse.class);
		mapper.addConverter(context -> {
			final Address address = context.getSource();
			final CityResponse cityResponse = mapper.map(address.getCity(), CityResponse.class);
			return new AddressResponse();
		}, Address.class, AddressResponse.class);
	}
}
