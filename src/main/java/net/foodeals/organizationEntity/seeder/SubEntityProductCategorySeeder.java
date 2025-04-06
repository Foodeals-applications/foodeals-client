package net.foodeals.organizationEntity.seeder;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.domain.entities.SubEntityDomain;
import net.foodeals.organizationEntity.domain.entities.SubEntityProductCategory;
import net.foodeals.organizationEntity.domain.repositories.SubEntityDomainRepository;
import net.foodeals.organizationEntity.domain.repositories.SubEntityProductCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(4)
@Component
@RequiredArgsConstructor
@Transactional
public class SubEntityProductCategorySeeder implements CommandLineRunner {


    private final SubEntityDomainRepository subEntityDomainRepository;
    private final SubEntityProductCategoryRepository subEntityProductCategoryRepository;

    @Override
    public void run(String... args) throws Exception {

        SubEntityDomain boulangerie = subEntityDomainRepository.findByName("Boulangeries").orElse(null);
        SubEntityDomain supermarche = subEntityDomainRepository.findByName("Supermarchés").orElse(null);
        SubEntityDomain hypermarche = subEntityDomainRepository.findByName("Hypermarchés").orElse(null);
        SubEntityDomain superette = subEntityDomainRepository.findByName("Superettes").orElse(null);
        SubEntityDomain restaurant = subEntityDomainRepository.findByName("Restaurants").orElse(null);
        SubEntityDomain hotel = subEntityDomainRepository.findByName("Hôtels").orElse(null);
        SubEntityDomain traiteur = subEntityDomainRepository.findByName("Traiteurs").orElse(null);
        SubEntityDomain grossiste = subEntityDomainRepository.findByName("Grossistes").orElse(null);
        SubEntityDomain agricultures = subEntityDomainRepository.findByName("Agricultures").orElse(null);
        SubEntityDomain industriels = subEntityDomainRepository.findByName("Industriels").orElse(null);

        if (boulangerie != null && boulangerie.getSubEntityProductCategories().isEmpty()) {
            subEntityProductCategoryRepository.saveAll(List.of(
                    SubEntityProductCategory.create("Pains et viennoiseries", boulangerie),
                    SubEntityProductCategory.create("Pâtisseries", boulangerie),
                    SubEntityProductCategory.create("Gâteaux", boulangerie),
                    SubEntityProductCategory.create("Petits Fours", boulangerie),
                    SubEntityProductCategory.create("Produits Salés", boulangerie)));
        }

        if (supermarche != null && hypermarche.getSubEntityProductCategories().isEmpty()) {
            subEntityProductCategoryRepository.saveAll(List.of(
                    SubEntityProductCategory.create("Fruits et légumes", supermarche),
                    SubEntityProductCategory.create("Viandes et poissons", supermarche),
                    SubEntityProductCategory.create("Biscuits et confiseries", supermarche),
                    SubEntityProductCategory.create("Produits laitiers", supermarche),
                    SubEntityProductCategory.create("Produits céréaliers", supermarche),
                    SubEntityProductCategory.create("Produits en conserve", supermarche),
                    SubEntityProductCategory.create("Produits surgelés", supermarche),
                    SubEntityProductCategory.create("Produits épicerie", supermarche),
                    SubEntityProductCategory.create("Eaux et Boissons", supermarche)));
        }

        if (hypermarche != null && hypermarche.getSubEntityProductCategories().isEmpty()) {
            subEntityProductCategoryRepository.saveAll(List.of(
                    SubEntityProductCategory.create("Fruits et légumes", hypermarche),
                    SubEntityProductCategory.create("Viandes et poissons", hypermarche),
                    SubEntityProductCategory.create("Biscuits et confiseries", hypermarche),
                    SubEntityProductCategory.create("Produits laitiers", hypermarche),
                    SubEntityProductCategory.create("Produits céréaliers", hypermarche),
                    SubEntityProductCategory.create("Produits en conserve", hypermarche),
                    SubEntityProductCategory.create("Produits surgelés", hypermarche),
                    SubEntityProductCategory.create("Produits épicerie", hypermarche),
                    SubEntityProductCategory.create("Eaux et Boissons", hypermarche)));
        }

        if (superette != null && superette.getSubEntityProductCategories().isEmpty()) {
            subEntityProductCategoryRepository.saveAll(List.of(
                    SubEntityProductCategory.create("Fruits et légumes", superette),
                    SubEntityProductCategory.create("Viandes et poissons", superette),
                    SubEntityProductCategory.create("Biscuits et confiseries", superette),
                    SubEntityProductCategory.create("Produits laitiers", superette),
                    SubEntityProductCategory.create("Produits céréaliers", superette),
                    SubEntityProductCategory.create("Produits en conserve", superette),
                    SubEntityProductCategory.create("Produits surgelés", superette),
                    SubEntityProductCategory.create("Produits épicerie", superette),
                    SubEntityProductCategory.create("Eaux et Boissons", superette)));
        }

        if (restaurant != null && restaurant.getSubEntityProductCategories().isEmpty()) {
            subEntityProductCategoryRepository
                    .saveAll(List.of(SubEntityProductCategory.create("Fast Food", restaurant),
                            SubEntityProductCategory.create("Plats", restaurant),
                            SubEntityProductCategory.create("Asiatique", restaurant),
                            SubEntityProductCategory.create("Salades", restaurant),
                            SubEntityProductCategory.create("Desserts", restaurant)));
        }

        if (hotel != null && hotel.getSubEntityProductCategories().isEmpty()) {
            subEntityProductCategoryRepository.
                    saveAll(List.of(SubEntityProductCategory.create("Hôtels 5 étoiles", hotel),
                            SubEntityProductCategory.create("Hôtels 4 étoiles", hotel),
                            SubEntityProductCategory.create("Hôtels 3 étoiles", hotel),
                            SubEntityProductCategory.create("Hôtels 2 étoiles", hotel),
                            SubEntityProductCategory.create("Hôtels 1 étoile", hotel)));
        }

        if (traiteur != null && traiteur.getSubEntityProductCategories().isEmpty()) {
            subEntityProductCategoryRepository.saveAll(List.of(SubEntityProductCategory.create("Buffets", traiteur),
                    SubEntityProductCategory.create("Traiteurs de mariage", traiteur),
                    SubEntityProductCategory.create("Traiteurs pour événements", traiteur)));
        }

        if (grossiste != null && grossiste.getSubEntityProductCategories().isEmpty()) {
            subEntityProductCategoryRepository.saveAll(List.of(
                    SubEntityProductCategory.create("Fruits et légumes en gros", grossiste),
                    SubEntityProductCategory.create("Viandes en gros", grossiste),
                    SubEntityProductCategory.create("Poissons en gros", grossiste)));
        }

        if (industriels != null && industriels.getSubEntityProductCategories().isEmpty()) {
            subEntityProductCategoryRepository.saveAll(List.of(
                    SubEntityProductCategory.create("Machines industrielles", industriels),
                    SubEntityProductCategory.create("Produits chimiques", industriels),
                    SubEntityProductCategory.create("Composants électroniques",
                            industriels)));
        }

    }
}
