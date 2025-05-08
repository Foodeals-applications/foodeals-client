package net.foodeals.product.infrastructure.seeders;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.domain.entities.SubEntityDomain;
import net.foodeals.organizationEntity.domain.repositories.SubEntityDomainRepository;
import net.foodeals.product.domain.repositories.ProductCategoryRepository;

@Order(4)
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
