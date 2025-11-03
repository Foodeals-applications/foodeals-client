package net.foodeals.product.infrastructure.seeders;

import java.util.List;
import java.util.UUID;

import net.foodeals.core.domain.entities.SubEntityDomain;
import net.foodeals.core.repositories.ProductCategoryRepository;
import net.foodeals.core.repositories.SubEntityDomainRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Order(5)
@Component
@RequiredArgsConstructor
public class ProuctCategoryAndSubEntitesDomainSeeder implements CommandLineRunner {

    private final ProductCategoryRepository repository;

    private final SubEntityDomainRepository subEntityDomainRepository;

    @Override
    public void run(String... args) throws Exception {


        if (subEntityDomainRepository.count() == 0) {
            subEntityDomainRepository.saveAll(List.of(SubEntityDomain.create(UUID.randomUUID(),
                            "Boulangeries"),
                    SubEntityDomain.create(UUID.randomUUID(), "Pâtisseries"),
                    SubEntityDomain.create(UUID.randomUUID(), "Supermarchés"),
                    SubEntityDomain.create(UUID.randomUUID(), "Hypermarchés"),
                    SubEntityDomain.create(UUID.randomUUID(), "Superettes"),
                    SubEntityDomain.create(UUID.randomUUID(), "Restaurants"),
                    SubEntityDomain.create(UUID.randomUUID(), "Hôtels"),
                    SubEntityDomain.create(UUID.randomUUID(), "Traiteurs"),
                    SubEntityDomain.create(UUID.randomUUID(), "Grossistes"),
                    SubEntityDomain.create(UUID.randomUUID(), "Agricultures"),
                    SubEntityDomain.create(UUID.randomUUID(), "Industriels")));
        }

    }

}
