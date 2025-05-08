
package net.foodeals.location.infrastructure.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.location.application.dtos.requests.CountryRequest;
import net.foodeals.location.application.dtos.requests.StateRequest;
import net.foodeals.location.application.services.CountryService;
import net.foodeals.location.application.services.StateService;
import net.foodeals.location.domain.entities.City;
import net.foodeals.location.domain.entities.Country;
import net.foodeals.location.domain.entities.Region;
import net.foodeals.location.domain.entities.State;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.RegionRepository;

@Component
@RequiredArgsConstructor
@Transactional
@Order(1)
public class CityAndRegionEntitySeeder implements CommandLineRunner {

	@Autowired
	private final CityRepository cityRepository;

	private final RegionRepository regionRepository;

	private final StateService stateService;

	private final CountryService countryService;

	@Override
	public void run(String... args) throws Exception {

		CountryRequest countryRequest = new CountryRequest("Morocco", "202410");
		Country country = this.countryService.create(countryRequest);

		StateRequest stateRequest = new StateRequest("Casablanca-Settat", "102436", country.getId());
		State state = this.stateService.create(stateRequest);
		country.getStates().add(state);
		this.countryService.save(country);

		if (this.cityRepository.count() == 0) {
			City city1 = new City();
			city1.setName("Casablanca");
			city1.setState(state);
			city1.setCode("20235");
			Coordinates coordinates = new Coordinates(33.5731F, -7.5898F);
			city1.setCoordinates(coordinates);

			city1 = this.cityRepository.save(city1);
			state.getCities().add(city1);
			this.stateService.save(state);

			Region region1 = new Region();
			region1.setName("Casablanca-Settat");
			region1.setCity(city1);
			this.regionRepository.save(region1);

		}

	}
}
