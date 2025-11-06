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
            repository.saveAll(List.of(
                    ProductCategory.create(UUID.randomUUID(), "Boulangeries", "boulangeries", "Produits et pains issus des boulangeries artisanales", "images/categories/boulangeries.jpg", "bread-icon.png", 1),
                    ProductCategory.create(UUID.randomUUID(), "Pâtisseries", "patisseries", "Gâteaux, viennoiseries et desserts sucrés", "images/categories/patisseries.jpg", "cake-icon.png", 2),
                    ProductCategory.create(UUID.randomUUID(), "Supermarchés", "supermarches", "Produits vendus dans les grandes surfaces alimentaires", "images/categories/supermarches.jpg", "supermarket-icon.png", 3),
                    ProductCategory.create(UUID.randomUUID(), "Hypermarchés", "hypermarche", "Grandes enseignes proposant une large gamme de produits", "images/categories/hypermarches.jpg", "hypermarket-icon.png", 4),
                    ProductCategory.create(UUID.randomUUID(), "Superettes", "superettes", "Petits commerces de proximité", "images/categories/superettes.jpg", "store-icon.png", 5),
                    ProductCategory.create(UUID.randomUUID(), "Restaurants", "restaurants", "Établissements servant des plats cuisinés sur place", "images/categories/restaurants.jpg", "restaurant-icon.png", 6),
                    ProductCategory.create(UUID.randomUUID(), "Hôtels", "hotels", "Hébergement et services hôteliers", "images/categories/hotels.jpg", "hotel-icon.png", 7),
                    ProductCategory.create(UUID.randomUUID(), "Traiteurs", "traiteurs", "Service de plats préparés pour événements et particuliers", "images/categories/traiteurs.jpg", "catering-icon.png", 8),
                    ProductCategory.create(UUID.randomUUID(), "Grossistes", "grossistes", "Fournisseurs de produits en gros pour commerçants et restaurants", "images/categories/grossistes.jpg", "wholesale-icon.png", 9),
                    ProductCategory.create(UUID.randomUUID(), "Industriels", "industriels", "Producteurs industriels et fabriquants", "images/categories/industriels.jpg", "industry-icon.png", 10),
                    ProductCategory.create(UUID.randomUUID(), "Agricultures", "agricultures", "Producteurs agricoles et fermiers", "images/categories/agricultures.jpg", "farm-icon.png", 11)
            ));


        }}

}