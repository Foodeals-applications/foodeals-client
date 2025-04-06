package net.foodeals.organizationEntity.seeder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.enums.OrderSource;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.enums.OrderType;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.organizationEntity.domain.entities.SubEntityDomain;
import net.foodeals.organizationEntity.domain.repositories.SubEntityDomainRepository;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.repositories.ProductCategoryRepository;
import net.foodeals.product.domain.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.location.domain.repositories.AddressRepository;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.CountryRepository;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.enums.BoxStatus;
import net.foodeals.offer.domain.enums.BoxType;
import net.foodeals.offer.domain.enums.Category;
import net.foodeals.offer.domain.enums.DealStatus;
import net.foodeals.offer.domain.enums.PublishAs;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.offer.domain.repositories.OfferRepository;
import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.enums.EntityType;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityStatus;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;
import net.foodeals.organizationEntity.domain.repositories.ActivityRepository;
import net.foodeals.organizationEntity.domain.repositories.OrganizationEntityRepository;
import net.foodeals.organizationEntity.domain.repositories.SubEntityRepository;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.UserRepository;
import net.foodeals.user.domain.valueObjects.Name;


@Component
@RequiredArgsConstructor
@Transactional
@org.springframework.core.annotation.Order(4)
public class OrganizationSeeder implements CommandLineRunner {

    private final OrganizationEntityRepository organizationEntityRepository;
    private final SubEntityRepository subEntityRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final AddressRepository addressRepository;

    private final DealRepository dealRepository;
    private final BoxRepository boxRepository;
    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final SubEntityDomainRepository subEntityDomainRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!organizationEntityRepository.findByName("Carrefour").isPresent()) {

            // Création des entités de base
            Activity activity1 = createActivity("Supermarchés");
            User partnerManager = createUser("Ahmed", "Ben Ali", "ahmed.ben.ali@carrefour.ma", "0650123456");
            User subEntityManager = createUser("Sara", "El Fassi", "sara.elfassi@carrefourmarket.ma", "0650765432");

            Address mainAddress = createAddress("123 Carrefour St", "Casablanca", "20000", "Maroc");
            OrganizationEntity carrefour = createOrganizationEntity("Carrefour", activity1, mainAddress, partnerManager);

            Address subEntityAddress = createAddress("12 Carrefour St Maaref", "Casablanca", "20000", "Maroc");

            List<SubEntityDomain> domains = new ArrayList<>();
            Optional<SubEntityDomain> domainSuperMarchesOpt = subEntityDomainRepository.findByName("Supermarchés");
            Optional<SubEntityDomain> domainSuperettesOpt = subEntityDomainRepository.findByName("Superettes");
            domains.add(domainSuperettesOpt.get());
            domains.add(domainSuperMarchesOpt.get());
            SubEntity carrefourMarket =
                    createSubEntity("Carrefour Market", carrefour, subEntityManager, activity1,
                            subEntityAddress, 39,domains);


            // Ajout de produits associés à la sous-entité
            Product product1 = createProduct(carrefourMarket, "Pommes Bio", "Pommes fraîches et biologiques.", new BigDecimal("5.99"), "Supermarchés");
            Product product2 = createProduct(carrefourMarket, "Lait entier", "Bouteille de lait entier 1L.", new BigDecimal("1.99"), "Supermarchés");
            Product product3 = createProduct(carrefourMarket, "Buche de Noël", "Délicieux gâteau de Noël.", new BigDecimal("15.99"), "Supermarchés");

            // Ajout d'Offers, Deals et Boxes
            Offer carrefourOffer1 = createOffer(carrefourMarket, new BigDecimal("29.99"), new BigDecimal("49.99"));
            Offer carrefourOffer2 = createOffer(carrefourMarket, new BigDecimal("29.99"), new BigDecimal("59.99"));

            // Deals associés aux produits
            createDealWithOfferAndProduct("Promo Carrefour Market", "Réduction pommes bio.", carrefourOffer1, DealStatus.AVAILABLE, Category.FRUITS_AND_VEGETABLES, product1);
            createDealWithOfferAndProduct("Promotion Noël Carrefour", "Offre spéciale sur le gâteau de Noël.", carrefourOffer2, DealStatus.AVAILABLE, Category.FROZEN_PRODUCTS, product3);

            // Boxes associées aux offres
            createBoxWithOffer("Box Carrefour Market", "Box avec lait et autres produits.", carrefourOffer1, BoxType.NORMAL_BOX, BoxStatus.AVAILABLE, Category.DAIRY_PRODUCTS, product2);
            createBoxWithOffer("Box Spéciale Noël", "Box garnie pour Noël avec gâteaux.", carrefourOffer2, BoxType.NORMAL_BOX, BoxStatus.AVAILABLE, Category.FROZEN_PRODUCTS, product3);

            System.out.println("Seed terminé pour Carrefour, ses produits, Deals et Boxes.");

            // MIG : Ajout des Orders
            createOrder(carrefourMarket, carrefourOffer1, product1, "Client A", "client.a@gmail.com");
            createOrder(carrefourMarket, carrefourOffer2, product2, "Client B", "client.b@gmail.com");

        }
    }

    // Méthode pour créer une activité
    private Activity createActivity(String name) {
        Activity activity = new Activity();
        activity.setName(name);
        return activityRepository.save(activity);
    }

    // Méthode pour créer un utilisateur
    private User createUser(String firstName, String lastName, String email, String phone) {
        User user = new User();
        user.setName(new Name(firstName, lastName));
        user.setEmail(email);
        user.setPhone(phone);
        return userRepository.save(user);
    }

    // Méthode pour créer une adresse
    private Address createAddress(String address, String city, String zip, String country) {
        Address addr = new Address();
        addr.setAddress(address);
        addr.setCity(cityRepository.findByName(city));
        addr.setZip(zip);
        addr.setCountry(countryRepository.findByName(country));
        return addressRepository.save(addr);
    }

    // Méthode pour créer une OrganizationEntity
    private OrganizationEntity createOrganizationEntity(String name, Activity mainActivity, Address address, User partnerManager) {
        OrganizationEntity org = OrganizationEntity.builder().name(name).avatarPath("/images/" + name.toLowerCase() + "-avatar.png").coverPath("/images/" + name.toLowerCase() + "-cover.png").type(EntityType.PARTNER).mainActivity(mainActivity).address(address).commercialNumber("123456789").users(List.of(partnerManager)).build();
        return organizationEntityRepository.save(org);
    }

    // Méthode pour créer une sous-entité
    private SubEntity createSubEntity(String name, OrganizationEntity orgEntity, User manager, Activity activity, Address address, Integer numberOfLikes,List<SubEntityDomain> domains) {
        SubEntity subEntity = new SubEntity();
        subEntity.setName(name);
        subEntity.setAvatarPath("/images/" + name.toLowerCase().replace(" ", "-") + "-avatar.png");
        subEntity.setCoverPath("/images/" + name.toLowerCase().replace(" ", "-") + "-cover.png");
        subEntity.setType(SubEntityType.PARTNER_SB);
        subEntity.setActivities(List.of(activity));
        subEntity.setCoordinates(new Coordinates(33.5731F, -7.5898F));
        subEntity.setManager(manager);
        subEntity.setOrganizationEntity(orgEntity);
        subEntity.setSubEntityStatus(SubEntityStatus.ACTIVE);
        subEntity.setAddress(address);
        subEntity.setNumberOfLikes(numberOfLikes);
        subEntity.setSubEntityDomains(domains);
        return subEntityRepository.save(subEntity);
    }

    // Méthode pour créer un produit associé à une sous-entité
    private Product createProduct(SubEntity subEntity, String name, String description, BigDecimal price, String category) {
        Product product = new Product();
        product.setName(name);
        product.setProductImagePath("/images/" + name.toLowerCase().replace(" ", "-") + "-avatar.png");
        product.setDescription(description);
        product.setPrice(new Price(price, Currency.getInstance("MAD")));
        product.setSubEntity(subEntity); // Associer explicitement le produit à la sous-entité
        ProductCategory productCategory = productCategoryRepository.findByName(category).orElse(null);
        product.setCategory(productCategory);
        return productRepository.save(product);
    }

    // Méthode pour créer une offre
    private Offer createOffer(SubEntity subEntity, BigDecimal salePrice, BigDecimal price) {
        Offer offer = new Offer();
        offer.setSubEntity(subEntity);
        offer.setPrice(new Price(price, Currency.getInstance("MAD")));
        offer.setSalePrice(new Price(salePrice, Currency.getInstance("MAD")));
        offer.setDeliveryFee(5L);
        return offerRepository.save(offer);
    }

    // Méthode pour créer un deal associé à un produit
    private Deal createDealWithOfferAndProduct(String title, String description, Offer offer, DealStatus status, Category category, Product product) {
        Deal deal = new Deal();
        deal.setTitle(title);
        deal.setDescription(description);
        deal.setPrice(offer.getPrice());
        deal.setDealStatus(status);
        deal.setCategory(category);
        deal.setPublishAs(PublishAs.SUPERMARKETS_HYPERMARKETS);
        deal.setProduct(product); // Associer le produit au Deal
        deal.setOffer(offer);
        return dealRepository.save(deal);
    }

    // Méthode pour créer une box associée à un produit
    private Box createBoxWithOffer(String title, String description, Offer offer, BoxType type, BoxStatus status, Category category, Product product) {
        Box box = new Box(type);
        box.setTitle(title);
        box.setDescription(description);
        box.setOffer(offer);
        box.setBoxStatus(status);
        box.setPublishAs(PublishAs.SUPERMARKETS_HYPERMARKETS);
        box.setCategory(category);
        box.setQuantity(50); // Exemple : nombre de produits
        box.setReason("Offre spéciale."); // Exemple : raison
        return boxRepository.save(box);
    }

    // Nouvelle méthode pour créer une commande
    private Order createOrder(SubEntity subEntity, Offer offer, Product product, String clientName, String clientEmail) {
        User client = createUser(clientName.split(" ")[0], clientName.split(" ")[1], clientEmail, "0650432178");
        Order order = new Order();
        order.setOrderType(OrderType.AT_PLACE);
        order.setStatus(OrderStatus.COMPLETED);
        order.setClient(client);
        order.setOffer(offer);
        order.setQuantity(1);  // Exemple de quantité par défaut
        order.setShippingAddress(offer.getSubEntity().getAddress());  // Utilisation de l'adresse de la SubEntity
        order.setOrderSource(OrderSource.DEAL_PRO);
        return orderRepository.save(order);
    }
}
