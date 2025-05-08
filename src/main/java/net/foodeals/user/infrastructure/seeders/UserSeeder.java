package net.foodeals.user.infrastructure.seeders;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.location.application.dtos.requests.CountryRequest;
import net.foodeals.location.application.dtos.requests.StateRequest;
import net.foodeals.location.application.services.CountryService;
import net.foodeals.location.application.services.StateService;
import net.foodeals.location.domain.entities.*;
import net.foodeals.location.domain.repositories.AddressRepository;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.RegionRepository;
import net.foodeals.notification.application.services.NotificationSettingsService;
import net.foodeals.notification.domain.entity.NotificationSettings;
import net.foodeals.user.domain.entities.Role;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.enums.UserStatus;
import net.foodeals.user.domain.repositories.RoleRepository;
import net.foodeals.user.domain.repositories.UserRepository;
import net.foodeals.user.domain.valueObjects.Name;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Transactional
@Order(3)
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final CityRepository cityRepository;
    private final StateService stateService;
    private final CountryService countryService;
    private final RegionRepository regionRepository;
    private final NotificationSettingsService notificationSettingsService;

    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.findByEmail("mohamed.benibrahim@example.com").isPresent()) {
            User user = createNewUser("Mohamed", "Ben Ibrahim", "mohamed.benibrahim@example.com", "CLIENT");
            user = userRepository.save(user);
            NotificationSettings notificationSettings = new NotificationSettings();
            notificationSettings.setUser(user);
            notificationSettings.setPushNotifications(true);
            notificationSettings.setPromotions(false);
            notificationSettings.setImportantUpdates(true);
            notificationSettings.setCalendarReminders(false);
            notificationSettingsService.updateSettings(user, notificationSettings);
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
        user.setDateOfBirth(LocalDate.of(1990, 12, 5));
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
        country =this.countryService.save(country);
        newAddress.setCountry(country);

        City city = cityRepository.findByName("Casablanca");
        newAddress.setCity(city);

        Region region = regionRepository.findByName("Casablanca-Settat ");

        newAddress.setRegion(region);
        return addressRepository.save(newAddress);
    }

}