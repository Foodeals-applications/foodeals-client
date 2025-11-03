package net.foodeals.product.infrastructure.seeders;

import java.util.List;
import java.util.UUID;

import net.foodeals.core.domain.entities.ProductCategory;
import net.foodeals.core.repositories.ProductCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Order(4)
@Component
@RequiredArgsConstructor
public class ProuctCategorySeeder implements CommandLineRunner {

	private final ProductCategoryRepository repository;

	@Override
	public void run(String... args) throws Exception {

	
		if (repository.count() == 0) {
			repository.saveAll(List.of(ProductCategory.create(UUID.randomUUID(), "Boulangeries", "boulangeries"),
					ProductCategory.create(UUID.randomUUID(), "Pâtisseries", "patisseries"),
					ProductCategory.create(UUID.randomUUID(), "Supermarchés", "supermarches"),
					ProductCategory.create(UUID.randomUUID(), "Hypermarchés", "hypermarche"),
					ProductCategory.create(UUID.randomUUID(), "Superettes", "superettes"),
					ProductCategory.create(UUID.randomUUID(), "Restaurants", "restaurants"),
					ProductCategory.create(UUID.randomUUID(), "Hôtels", "hotels"),
					ProductCategory.create(UUID.randomUUID(), "Traiteurs", "traiteurs"),
					ProductCategory.create(UUID.randomUUID(), "Grossistes", "grossistes"),
					ProductCategory.create(UUID.randomUUID(), "Industriels", "industriels"),
					ProductCategory.create(UUID.randomUUID(), "Agricultures", "agricultures")));
		}

	}

}