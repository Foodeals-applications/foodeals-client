package net.foodeals.organizationEntity.seeder;

import lombok.RequiredArgsConstructor;
import net.foodeals.banner.domain.entities.Banner;
import net.foodeals.banner.domain.repositories.BannerRepository;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.location.domain.repositories.AddressRepository;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.CountryRepository;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.entities.OpenTime;
import net.foodeals.offer.domain.enums.*;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.offer.domain.repositories.OfferRepository;
import net.foodeals.offer.domain.repositories.OpenTimeRepository;
import net.foodeals.order.domain.entities.Coupon;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.entities.Transaction;
import net.foodeals.order.domain.enums.*;
import net.foodeals.order.domain.repositories.CouponRepository;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.order.domain.repositories.TransactionRepository;
import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntityDomain;
import net.foodeals.organizationEntity.domain.entities.enums.EntityType;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityStatus;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;
import net.foodeals.organizationEntity.domain.repositories.*;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.entities.Supplement;
import net.foodeals.product.domain.enums.SupplementCategory;
import net.foodeals.product.domain.repositories.ProductCategoryRepository;
import net.foodeals.product.domain.repositories.ProductRepository;
import net.foodeals.product.domain.repositories.SupplementRepository;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.UserRepository;
import net.foodeals.user.domain.valueObjects.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


@Component
@RequiredArgsConstructor
@Transactional
@org.springframework.core.annotation.Order(7)
public class OrganizationSeeder implements CommandLineRunner {

    private final OrganizationEntityRepository organizationEntityRepository;
    private final SubEntityRepository subEntityRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final AddressRepository addressRepository;

    private final DealRepository dealRepository;
    private final SupplementRepository supplementRepository;
    private final BoxRepository boxRepository;
    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final SubEntityDomainRepository subEntityDomainRepository;
    private final SubEntityProductCategoryRepository subEntityProductCategoryRepository;
    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final OpenTimeRepository openTimeRepository;
    private final CouponRepository couponRepository;
    private final BannerRepository bannerRepository;

    @Override
    public void run(String... args) throws Exception {
        SubEntity goldenTolipCasa = null;
        SubEntity kfcCasa = null;
        if (!organizationEntityRepository.findByName("Carrefour").isPresent()) {

            /**
             * Création superretes et superMarchés
             */
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
                            subEntityAddress, 39, true, 4.5f, domains);


            // Ajout de produits associés à la sous-entité
            Product product1 = createProduct(carrefourMarket,
                    "Pommes Bio",
                    "Pommes fraîches et biologiques.",
                    new BigDecimal("5.99"),
                    "Supermarchés", 20);
            Product product2 = createProduct(carrefourMarket, "Lait entier",
                    "Bouteille de lait entier 1L.", new BigDecimal("1.99"), "Supermarchés", 10);
            Product product3 = createProduct(carrefourMarket, "Buche de Noël",
                    "Délicieux gâteau de Noël.", new BigDecimal("15.99"), "Supermarchés", 6);

            Product product4 = createProduct(carrefourMarket,
                    "Lait entier fraise",
                    "Pommes fraîches et biologiques.",
                    new BigDecimal("5.99"),
                    "Supermarchés", 20);

            // Ajout d'Offers, Deals et Boxes
            Offer carrefourOffer1 = createOffer(carrefourMarket, new BigDecimal("29.99"), new BigDecimal("49.99"), 32, 4.2f);
            createOpenTimes(carrefourOffer1);
            Offer carrefourOffer2 = createOffer(carrefourMarket, new BigDecimal("29.99"), new BigDecimal("59.99"), 20, 3.2f);
            createOpenTimes(carrefourOffer2);
            // Deals associés aux produits
            Deal pommeDeal = createDealWithOfferAndProduct("Promo Carrefour Market", "Réduction pommes bio.", carrefourOffer1, 1, DealStatus.AVAILABLE, Category.FRUITS_AND_VEGETABLES, product1);
            Deal noelDeal = createDealWithOfferAndProduct("Promotion Noël Carrefour", "Offre spéciale sur le gâteau de Noël.", carrefourOffer2, 2, DealStatus.AVAILABLE, Category.FROZEN_PRODUCTS, product3);


            // Boxes associées aux offres
            Box box1 = createBoxWithOffer("Box Carrefour Market", "Box avec lait et autres produits.", carrefourOffer1, BoxType.NORMAL_BOX, BoxStatus.AVAILABLE, Category.DAIRY_PRODUCTS, product2);
            Box box2 = createBoxWithOffer("Box Carrefour Market 2", "Box avec lait Fraises et autres produits.", carrefourOffer1, BoxType.NORMAL_BOX, BoxStatus.AVAILABLE, Category.DAIRY_PRODUCTS, product4);
            Box box3 = createBoxWithOffer("Box Spéciale Noël", "Box garnie pour Noël avec gâteaux.", carrefourOffer2, BoxType.NORMAL_BOX, BoxStatus.AVAILABLE, Category.FROZEN_PRODUCTS, product3);

            createSupplement("Lait", SupplementCategory.SUPPLEMENTS, noelDeal, box1);

            System.out.println("Seed terminé pour Carrefour, ses produits, Deals et Boxes.");

            // MIG : Ajout des Orders
            Order order1 = createOrder(carrefourMarket, carrefourOffer1, product1, OrderStatus.IN_PROGRESS);
            Order order2 = createOrder(carrefourMarket, carrefourOffer2, product2, OrderStatus.CANCELED);
            Order order3 = createOrder(carrefourMarket, carrefourOffer2, product2, OrderStatus.COMPLETED);
            Transaction transaction1 = createTransactionForOrder(order1, "PAY123456789", order1.getOffer().getPrice());
            Transaction transaction2 = createTransactionForOrder(order2, "PAY123456790", order2.getOffer().getPrice());
            Transaction transaction3 = createTransactionForOrder(order2, "PAY123456791", order3.getOffer().getPrice());
            transaction1 = transactionRepository.save(transaction1);
            transaction2 = transactionRepository.save(transaction2);
            transaction3 = transactionRepository.save(transaction3);
            order1.setTransaction(transaction1);
            order2.setTransaction(transaction2);
            order3.setTransaction(transaction3);
            orderRepository.save(order1);
            orderRepository.save(order2);
            orderRepository.save(order3);


            /**
             * Création hotel
             */

            Activity activityHotel = createActivity("Hôtels");
            User hotelManager = createUser("Ismail", "Ben Mabrouk", "ismail.mabrouk@golden-tolip.ma", "0650123456");
            User subEntityHotelManager = createUser("Mourad", "Ramhi", "mourad.ramhi@golden-tolip.ma", "0650765432");

            Address hotelMainAddress = createAddress("123 Rue Golden St", "Casablanca", "20000", "Maroc");
            OrganizationEntity goldenTolip = createOrganizationEntity("Golden-Tolip", activityHotel, hotelMainAddress,
                    hotelManager);

            Address subEntityHotelAddress = createAddress("12  Rue Golden St Maaref", "Casablanca", "20000", "Maroc");

            List<SubEntityDomain> domainsHotel = new ArrayList<>();
            Optional<SubEntityDomain> domainHotel = subEntityDomainRepository.findByName("Hôtels");
            domainsHotel.add(domainHotel.get());

            goldenTolipCasa = createSubEntity("Golden Tolip Casa", goldenTolip, subEntityHotelManager, activityHotel,
                    subEntityHotelAddress, 400, false, 3.8f, domainsHotel);


            /**
             * Création restaurant
             */

            Activity activityRestaurant = createActivity("Restaurants");
            User restautantManager = createUser("Chafik", "Jarraya", "chafik.jarraya@kfc.ma", "0650123456");
            User subEntityRestaurantManager = createUser("Salim", "El iamani", "salim.eliamani@kfc.ma", "0650765432");

            Address restautantMainAddress = createAddress("123 Mohamed V St", "Casablanca", "20000", "Maroc");
            OrganizationEntity kfc = createOrganizationEntity("KFC", activityRestaurant,
                    restautantMainAddress,
                    restautantManager);

            Address subEntityRestaurantAddress = createAddress("12  Rue Ibnou Sina Maaref", "Casablanca", "20000", "Maroc");

            List<SubEntityDomain> domainsRestaurant = new ArrayList<>();
            Optional<SubEntityDomain> domainRestaurant = subEntityDomainRepository.findByName("Restaurants");
            domainsRestaurant.add(domainRestaurant.get());

            kfcCasa = createSubEntity("KFC Casa", kfc, subEntityRestaurantManager, activityRestaurant,
                    subEntityRestaurantAddress, 400, true, 4.8F, domainsRestaurant);


            Offer kfcOffer1 = createOffer(kfcCasa, new BigDecimal("129.99"), new BigDecimal("89.99"), 8, 3.3f);
            createOpenTimes(kfcOffer1);
            Offer kfcOffer2 = createOffer(kfcCasa, new BigDecimal("139.99"), new BigDecimal("89.99"), 10, 5f);
            createOpenTimes(kfcOffer2);

            Product productKfc1 = createProduct(kfcCasa, "Tinders", "Tinders Kabab.",
                    new BigDecimal("5.99"), "Restaurants", 20);
            Product productKfc2 = createProduct(kfcCasa, "Chicken wings",
                    "Box chings wings.", new BigDecimal("1.99"), "Restaurants", 10);
            Deal dealWings = createDealWithOfferAndProduct("23 wings magics", "Réduction spéciale.", kfcOffer1, 1, DealStatus.AVAILABLE, Category.FRUITS_AND_VEGETABLES, productKfc1);
            Deal dealTinders = createDealWithOfferAndProduct("Deux tinders achétes un gratuit", "Offre spéciale étudiant.", kfcOffer2, 2, DealStatus.AVAILABLE, Category.FROZEN_PRODUCTS, productKfc2);
            createSupplement("Coca", SupplementCategory.DRINK, dealWings, null);
            createSupplement("Fanta", SupplementCategory.DRINK, dealWings, null);

            createSupplement("Sauce mayonnaise", SupplementCategory.SAUCE, dealTinders, null);
            createSupplement("Sauce algerienne", SupplementCategory.SAUCE, dealTinders, null);
        }


        /**
         * Industry
         */

        /**
         * Création indsutry
         */

        Activity activityIndustry = createActivity("Industriels");
        User industryManager = null;
        if (!userRepository.findByEmail("kamel.ltaiefa@delice.ma").isPresent()) {
            industryManager = createUser("Kamel", "Ltaief", "kamel.ltaiefa@delice.ma", "0650123456");

        }

        User subEntityIndustryManager = null;
        if (!userRepository.findByEmail("mounir.bansalha@delice.ma").isPresent()) {
            subEntityIndustryManager = createUser("Mounir", "Ben Salha",
                    "mounir.bansalha@delice.ma", "0650865432");
        }


        Address industryMainAddress = createAddress("123 Charles Egaul ", "Casablanca", "20000", "Maroc");
        OrganizationEntity delice = createOrganizationEntity("Delice", activityIndustry,
                industryMainAddress,
                industryManager);

        Address subEntityIndustryAddress = createAddress("Qaurtie industriel Casa", "Casablanca", "20000", "Maroc");

        List<SubEntityDomain> domainsIndustry = new ArrayList<>();
        Optional<SubEntityDomain> domainIndustry = subEntityDomainRepository.findByName("Industriels");
        domainsIndustry.add(domainIndustry.get());

        SubEntity deliceCasa =
                createSubEntity("Delice Casa", delice, subEntityIndustryManager,
                        activityIndustry,
                        subEntityIndustryAddress, 400, true, 4.8F,
                        domainsIndustry);


        /**
         * Création agriculture         */

        Activity activityAgriculture = createActivity("Agricultures");
        User agricultureManager = createUser("Samir", "Hamdani",
                "samir.hamdani@zalar-holding.ma", "0658123456");
        User subEntityAgriculureManager = createUser("Wafa", "Moutawakil",
                "wafa.moutawakil@zalar-holding.ma", "0659865432");

        Address agricultureMainAddress = createAddress("15 rue agriculture ", "Casablanca", "20000", "Maroc");
        OrganizationEntity zalar = createOrganizationEntity("Zalar Holding", activityAgriculture,
                agricultureMainAddress,
                agricultureManager);

        Address subEntityAgrocultureAddress = createAddress("Qaurtie agriculture Casa", "Casablanca", "20000", "Maroc");

        List<SubEntityDomain> domainsAgriculture = new ArrayList<>();
        Optional<SubEntityDomain> domainAgriculture = subEntityDomainRepository.findByName("Agricultures");
        domainsAgriculture.add(domainAgriculture.get());

        SubEntity zalarCasa =
                createSubEntity("Zalar Holding Casa", delice, subEntityAgriculureManager,
                        activityAgriculture,
                        subEntityAgrocultureAddress, 400, true, 4.8F,
                        domainsAgriculture);


        // OFFERS & DEALS POUR HÔTEL (Golden Tolip)
        Offer hotelOffer1 = createOffer(goldenTolipCasa, new BigDecimal("399.00"), new BigDecimal("299.00"), 12, 4.0f);
        createOpenTimes(hotelOffer1);

        Product hotelProduct1 = createProduct(goldenTolipCasa, "Nuitée Standard",
                "Chambre standard avec petit déjeuner", new BigDecimal("299.00"),
                "Hôtels", 10);
        Deal hotelDeal1 = createDealWithOfferAndProduct("Offre Nuitée", "Promotion sur chambre avec PDJ.", hotelOffer1, 1, DealStatus.AVAILABLE, Category.WHOLESALER_DAIRY_PRODUCTS, hotelProduct1);

        // OFFERS & DEALS POUR INDUSTRIE (Délice)
        Offer industryOffer1 = createOffer(deliceCasa, new BigDecimal("2500.00"), new BigDecimal("1999.00"), 7, 4f);
        createOpenTimes(industryOffer1);

        Product industryProduct1 = createProduct(deliceCasa, "Pack industriel lait", "Lot de lait pour industrie agroalimentaire", new BigDecimal("1999.00"), "Industriels", 5);
        Deal industryDeal1 = createDealWithOfferAndProduct("Pack Lait Industrie", "Remise sur gros volume lait", industryOffer1, 1, DealStatus.AVAILABLE, Category.DAIRY_PRODUCTS, industryProduct1);

        // OFFERS & DEALS POUR AGRICULTURE (Zalar)
        Offer agricultureOffer1 = createOffer(zalarCasa, new BigDecimal("1499.00"), new BigDecimal("999.00"), 6, 4.9f);
        createOpenTimes(agricultureOffer1);

        Product agricultureProduct1 = createProduct(zalarCasa, "Pack Poulets Fermiers", "Volaille élevée en plein air", new BigDecimal("999.00"), "Agricultures", 15);
        Deal agricultureDeal1 = createDealWithOfferAndProduct("Offre Poulets Bio", "Réduction sur élevage durable", agricultureOffer1, 1, DealStatus.AVAILABLE, Category.WHOLESALER_DAIRY_PRODUCTS, agricultureProduct1);

        createCoupon(kfcCasa, "CARREFOUR10", 10f, new Date(System.currentTimeMillis() + 86400000L), true);   // Enabled, expire demain
        createCoupon(kfcCasa, "CARREFOUR20", 20f, new Date(System.currentTimeMillis() - 86400000L), false); // Disabled, expire demain

        createBanner(
                "https://cdn.monsite.com/banner1.jpg",
                "https://promo.monsite.com");

        createBanner(
                "https://cdn.monsite.com/banner2.jpg",
                "https://offre.monsite.com");
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
    private SubEntity createSubEntity(String name, OrganizationEntity orgEntity, User manager, Activity activity, Address address, Integer numberOfLikes, boolean feeDelivered, Float numberOfStars, List<SubEntityDomain> domains) {
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
        subEntity.setFeeDelivered(feeDelivered);
        subEntity.setNumberOfStars(numberOfStars);
        subEntity.setModalityTypes(List.of(ModalityType.AT_PLACE, ModalityType.DELIVERY, ModalityType.PICKUP));
        subEntity.setModalityPaiements(List.of(ModalityPaiement.CASH, ModalityPaiement.CARD));
        boolean isFeatured = ThreadLocalRandom.current().nextBoolean();
        subEntity.setFeatured(isFeatured);
        return subEntityRepository.save(subEntity);
    }

    // Méthode pour créer un produit associé à une sous-entité
    private Product createProduct(SubEntity subEntity, String name, String description,
                                  BigDecimal price, String category, Integer stock) {
        Product product = new Product();
        product.setName(name);
        product.setProductImagePath("/images/" + name.toLowerCase().replace(" ", "-") + "-avatar.png");
        product.setDescription(description);
        product.setPrice(new Price(price, Currency.getInstance("MAD")));
        product.setSubEntity(subEntity); // Associer explicitement le produit à la sous-entité
        ProductCategory productCategory = productCategoryRepository.findByName(category).orElse(null);
        product.setCategory(productCategory);
        product.setStock(stock);
        return productRepository.save(product);
    }

    private Supplement createSupplement(String name, SupplementCategory category, Deal deal, Box box) {
        Supplement supplement = new Supplement();
        supplement.setName(name);
        supplement.setSupplementImagePath("/images/" + name.toLowerCase().replace(" ", "-") + ".png");
        supplement.setPrice(new Price(new BigDecimal("50"), Currency.getInstance("MAD")));
        supplement.setDeal(deal);
        supplement.setBox(box);
        supplement.setSupplementCategory(category);
        return supplementRepository.save(supplement);
    }

    // Méthode pour créer une offre
    private Offer createOffer(SubEntity subEntity,
                              BigDecimal salePrice,
                              BigDecimal price,
                              Integer numberOfFeedback,
                              Float numberOfStars) {
        Offer offer = new Offer();
        offer.setSubEntity(subEntity);
        offer.setPrice(new Price(price, Currency.getInstance("MAD")));
        offer.setSalePrice(new Price(salePrice, Currency.getInstance("MAD")));
        offer.setModalityPaiement(ModalityPaiement.CASH);
        offer.setDeliveryFee(5L);
        offer.setModalityTypes(List.of(ModalityType.AT_PLACE, ModalityType.DELIVERY, ModalityType.PICKUP));
        offer.setNumberOfStars(numberOfStars);
        offer.setNumberOfFeedBack(numberOfFeedback);
        return offerRepository.save(offer);
    }

    private void createOpenTimes(Offer offer) {
        openTimeRepository.saveAllAndFlush(List.of(new OpenTime(new Date(), "08:00", "19:00", offer)));
    }

    // Méthode pour créer un deal associé à un produit
    private Deal createDealWithOfferAndProduct(String title, String description, Offer offer, Integer quantity, DealStatus status, Category category, Product product) {
        Deal deal = new Deal();
        deal.setTitle(title);

        deal.setDescription(description);
        deal.setPrice(offer.getPrice());
        deal.setDealStatus(status);
        deal.setCategory(category);
        deal.setPublishAs(PublishAs.SUPERMARKETS_HYPERMARKETS);
        deal.setProduct(product);
        deal.setQuantity(quantity);
        deal.setOffer(offer);
        return dealRepository.save(deal);
    }

    // Méthode pour créer une box associée à un produit
    private Box createBoxWithOffer(String title,
                                   String description,
                                   Offer offer,
                                   BoxType type,
                                   BoxStatus status,
                                   Category category,
                                   Product product

    ) {
        Box box = new Box(type);
        box.setTitle(title);
        box.setPhotoBoxPath("/images/" + title.toLowerCase().replace(" ", "-") + ".png");
        box.setDescription(description);
        box.setOffer(offer);
        box.setBoxStatus(status);
        box.setPublishAs(PublishAs.SUPERMARKETS_HYPERMARKETS);
        box.setCategory(category);
        box.setQuantity(50); // Exemple : nombre de produits
        box.setReason("Offre spéciale."); // Exemple : raison
        box.setProducts(List.of(product));
        return boxRepository.save(box);
    }

    // Nouvelle méthode pour créer une commande
    private Order createOrder(SubEntity subEntity, Offer offer, Product product, OrderStatus orderStatus) {
        User client = userRepository.findByEmail("mohamed.benibrahim@example.com").get();
        Order order = new Order();
        order.setOrderType(OrderType.AT_PLACE);
        order.setStatus(orderStatus);
        order.setCollectionStartTime(LocalTime.MIN);
        order.setCollectionEndTime(LocalTime.MAX);
        order.setClient(client);
        order.setOffer(offer);
        order.setQuantity(1);  // Exemple de quantité par défaut
        order.setShippingAddress(offer.getSubEntity().getAddress());  // Utilisation de l'adresse de la SubEntity
        order.setOrderSource(OrderSource.DEAL_PRO);
        return orderRepository.save(order);
    }


    private Transaction createTransaction(String paymentId,
                                          String reference,
                                          String context,
                                          Price price,
                                          TransactionStatus status,
                                          TransactionType type,
                                          Order order) {
        Transaction transaction = new Transaction();

        transaction.setPaymentId(paymentId);       // Défini l'ID du paiement
        transaction.setReference(reference);       // Défini la référence unique
        transaction.setContext(context);           // Défini le contexte de la transaction
        transaction.setPrice(price);               // Défini le prix de la transaction
        transaction.setStatus(status);             // Défini le statut de la transaction
        transaction.setType(type);                 // Défini le type de la transaction
        transaction.setOrder(order);               // Associe à une commande

        return transaction;
    }


    private Transaction createTransactionForOrder(Order order, String paymentId, Price price) {
        // Générer une référence unique
        String reference = "C-180926-345";

        // Initialiser et retourner une nouvelle transaction
        return createTransaction(
                paymentId,
                reference,
                "Paiement effectué",
                price,
                TransactionStatus.COMPLETED,
                TransactionType.CASH,
                order
        );
    }

    private Coupon createCoupon(SubEntity subEntity, String code, Float discount, Date endsAt, boolean isEnabled) {
        Coupon coupon = new Coupon();
        coupon.setCode(code);
        coupon.setName(code);
        coupon.setDiscount(discount);
        coupon.setEndsAt(endsAt);
        coupon.setIsEnabled(isEnabled);
        coupon.setSubEntity(subEntity);
        return couponRepository.save(coupon);
    }


    private void createBanner(String imageUrl, String link) {
        // Vérifie si la bannière existe déjà (pour éviter doublons)
        boolean exists = bannerRepository
                .findAll()
                .stream()
                .anyMatch(b -> b.getImageUrl().equals(imageUrl) && b.getLink().equals(link));

        if (!exists) {
            Banner banner = new Banner(imageUrl, link);
            bannerRepository.save(banner);
            System.out.println("✅ Banner créée : " + link);
        } else {
            System.out.println("ℹ️ Bannière déjà existante : " + link);
        }
    }

}
