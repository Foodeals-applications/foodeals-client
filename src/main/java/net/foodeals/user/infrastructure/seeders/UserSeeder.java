package net.foodeals.user.infrastructure.seeders;

import java.util.Objects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.location.application.dtos.requests.CountryRequest;
import net.foodeals.location.application.dtos.requests.StateRequest;
import net.foodeals.location.application.services.CountryService;
import net.foodeals.location.application.services.StateService;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.location.domain.entities.City;
import net.foodeals.location.domain.entities.Country;
import net.foodeals.location.domain.entities.Region;
import net.foodeals.location.domain.entities.State;
import net.foodeals.location.domain.repositories.AddressRepository;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.RegionRepository;
import net.foodeals.user.domain.entities.Role;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.enums.UserStatus;
import net.foodeals.user.domain.repositories.RoleRepository;
import net.foodeals.user.domain.repositories.UserRepository;
import net.foodeals.user.domain.valueObjects.Name;

@Component
@RequiredArgsConstructor
@Transactional
@Order(2)
public class UserSeeder implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final AddressRepository addressRepository;
	private final PasswordEncoder passwordEncoder;
	private final CityRepository cityRepository;
	private final StateService stateService;
	private final CountryService countryService;
	private final RegionRepository regionRepository;

	@Override
	public void run(String... args) throws Exception {

		if(Objects.isNull(userRepository.findByEmail("mohamed.benibrahim@example.com").get())) {
		User user = createNewUser("Mohamed", "Ben Ibrahim", "mohamed.benibrahim@example.com", "CLIENT");
		userRepository.save(user);
		}

	}

	private User createNewUser(String firstName, String lastName, String email, String roleName) {
		Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new RuntimeException("Role MANAGER_REGIONALE non trouvé"));
		User user = new User();
		user.setName(new Name(firstName, lastName));
		user.setEmail(email);
		user.setPhone("0616298366");
		user.setPassword(passwordEncoder.encode("password"));
		user.setRole(role);
		user.setIsEmailVerified(true);
		user.setStatus(UserStatus.ACTIVE);
		user.setAccount(null);
		Coordinates coordinates = new Coordinates(33.9871f, -8.5498f);
		user.setCoordinates(coordinates);
		Address address = createAddress("Rue Moussa Bnou Noussair", "Quartier Gauthier", "20000", coordinates);
		user.setAddress(address);
		return userRepository.save(user);
	}

	private Address createAddress(String address, String extraAddress, String zip, Coordinates coordinates) {
		Address newAddress = new Address();
		newAddress.setAddress(address);
		newAddress.setExtraAddress(extraAddress);
		newAddress.setZip(zip);
		newAddress.setCoordinates(coordinates);

		CountryRequest countryRequest = new CountryRequest("Morocco", "202410");
		Country country = this.countryService.create(countryRequest);

		StateRequest stateRequest = new StateRequest("Casablanca", "102436", country.getId());
		State state = this.stateService.create(stateRequest);
		country.getStates().add(state);
		this.countryService.save(country);

		City city = cityRepository.findByName("Casablanca");
		newAddress.setCity(city);

		Region region = regionRepository.findByName("Casablanca-Settat ");

		newAddress.setRegion(region);
		return addressRepository.save(newAddress);
	}

}