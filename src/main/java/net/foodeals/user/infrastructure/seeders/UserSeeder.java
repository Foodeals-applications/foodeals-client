package net.foodeals.user.infrastructure.seeders;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.location.application.dtos.requests.CountryRequest;
import net.foodeals.location.application.dtos.requests.StateRequest;
import net.foodeals.location.application.services.CountryService;
import net.foodeals.location.application.services.StateService;
import net.foodeals.location.domain.entities.*;
import net.foodeals.location.domain.enums.AddressType;
import net.foodeals.location.domain.repositories.AddressRepository;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.CountryRepository;
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
    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;
    private final StateService stateService;
    private final CountryService countryService;

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
        Address address = createAddress("123 Carrefour St", "Casablanca", "20000", "Morocco","Casablanca-Settat");;
        user.setAddress(address);
        user.setDateOfBirth(LocalDate.of(1990, 12, 5));
        return userRepository.save(user);
    }

    private Address createAddress(String address, String city, String zip, String country,String region) {
        Address addr = new Address();
        addr.setAddress(address);
        addr.setExtraAddress("Quartier " + city); // ou autre logique
        addr.setZip(zip);
        addr.setCoordinates(new Coordinates(33.5731F, -7.5898F));

        // 🟢 Lier les entités City / Country si existantes
        addr.setCity(cityRepository.findByName(city));
        addr.setCountry(countryRepository.findByName(country));
        addr.setRegion(regionRepository.findByName(region));

        // 🟢 Renseigner les infos de contact et type d’adresse
        addr.setAddressType(AddressType.HOME); // ou "OTHER" selon le cas
        addr.setContactName("Service Client " + city);
        addr.setContactEmail("contact@" + city.toLowerCase() + ".ma");
        addr.setContactPhone("+212600000000");
        addr.setIdMapCity("city.12345");
        addr.setIdMapCountry("country.12345");
        addr.setIdMapRegion("region.12345");
        return addressRepository.save(addr);
    }

}