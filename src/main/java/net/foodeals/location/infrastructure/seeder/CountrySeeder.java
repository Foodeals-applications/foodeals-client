package net.foodeals.location.infrastructure.seeder;

import net.foodeals.location.application.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class CountrySeeder {

    @Autowired
    private CountryService countryService;

//    @Transactional
//    @EventListener(ApplicationReadyEvent.class)
//    public void createCountry() {
//        CountryRequest countryRequest = new CountryRequest("Morocco", "202410");
//        Country country = this.countryService.create(countryRequest);
//    }
}
