package net.foodeals.organizationEntity.seeder;

import lombok.RequiredArgsConstructor;

import net.foodeals.core.domain.entities.*;
import net.foodeals.core.domain.enums.*;
import net.foodeals.core.repositories.*;
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
    private final RegionRepository regionRepository;
    private final AddressRepository addressRepository;
    private final DonateRepository donateRepository;
    private final RatingRepository ratingRepository;

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

    private final SupportTicketRepository supportTicketRepository;
    private final BusinessRecommendationRepository businessRecommendationRepository;
    private final ReferralRepository referralRepository;


    private final DeliveryRepository deliveryRepository;
    private final DeliveryPositionRepository deliveryPositionRepository;
    private final TrackingStepRepository trackingStepRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    private final PaymentMethodRepository paymentMethodRepository;
    private final DeliveryOptionRepository deliveryOptionRepository;

    private final DlcRepository dlcRepository;


    @Override
    public void run(String... args) throws Exception {
        SubEntity goldenTolipCasa = null;
        SubEntity kfcCasa = null;
        Box box2 = null;
        Product product2 = null;
        if (!organizationEntityRepository.findByName("Carrefour").isPresent()) {

            /**
             * Création superretes et superMarchés
             */
            Activity activity1 = createActivity("Supermarchés");
            User partnerManager = createUser("Ahmed", "Ben Ali", "ahmed.ben.ali@carrefour.ma", "0650123456");
            User subEntityManager = createUser("Sara", "El Fassi", "sara.elfassi@carrefourmarket.ma", "0650765432");

            Address mainAddress = createAddress("123 Carrefour St", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
            OrganizationEntity carrefour = createOrganizationEntity("Carrefour", activity1, mainAddress, partnerManager);

            Address subEntityAddress = createAddress("12 Carrefour St Maaref", "Casablanca", "20000", "Morocco", "Casablanca-Settat");

            List<SubEntityDomain> domains = new ArrayList<>();
            Optional<SubEntityDomain> domainSuperMarchesOpt = subEntityDomainRepository.findByName("Supermarchés");
            Optional<SubEntityDomain> domainSuperettesOpt = subEntityDomainRepository.findByName("Superettes");
            domains.add(domainSuperettesOpt.get());
            domains.add(domainSuperMarchesOpt.get());
            SubEntity carrefourMarket = createSubEntity("Carrefour Market", carrefour, subEntityManager, activity1, subEntityAddress, 39, true, 4.5f, domains);


            // Ajout de produits associés à la sous-entité
            Product product1 = createProduct(carrefourMarket, "Pommes Bio", "Pommes fraîches et biologiques.", new BigDecimal("5.99"), "Supermarchés", 20);
            product2 = createProduct(carrefourMarket, "Lait entier", "Bouteille de lait entier 1L.", new BigDecimal("1.99"), "Supermarchés", 10);
            Product product3 = createProduct(carrefourMarket, "Buche de Noël", "Délicieux gâteau de Noël.", new BigDecimal("15.99"), "Supermarchés", 6);

            Product product4 = createProduct(carrefourMarket, "Lait entier fraise", "Pommes fraîches et biologiques.", new BigDecimal("5.99"), "Supermarchés", 20);

            // Ajout d'Offers, Deals et Boxes
            Offer carrefourOffer1 = createOffer(carrefourMarket, new BigDecimal("29.99"), new BigDecimal("49.99"), 32, 4.2f);
            createOpenTimes(carrefourOffer1);
            Offer carrefourOffer2 = createOffer(carrefourMarket, new BigDecimal("29.99"), new BigDecimal("59.99"), 20, 3.2f);
            Offer carrefourOffer3 = createOffer(carrefourMarket, new BigDecimal("69.99"), new BigDecimal("79.99"), 20, 3.2f);
            createOpenTimes(carrefourOffer2);
            // Deals associés aux produits
            Deal pommeDeal = createDealWithOfferAndProduct("Promo Carrefour Market", "Réduction pommes bio.", carrefourOffer1, 1, DealStatus.AVAILABLE, Category.FRUITS_AND_VEGETABLES, product1);
            Deal noelDeal = createDealWithOfferAndProduct("Promotion Noël Carrefour", "Offre spéciale sur le gâteau de Noël.", carrefourOffer2, 2, DealStatus.AVAILABLE, Category.FROZEN_PRODUCTS, product3);


            // Boxes associées aux offres
            Box box1 = createBoxWithOffer("Box Carrefour Market", "Box avec lait et autres produits.", carrefourOffer1, BoxType.NORMAL_BOX, BoxStatus.AVAILABLE, Category.DAIRY_PRODUCTS, product2);
            box2 = createBoxWithOffer("Box Carrefour Market 2", "Box avec lait Fraises et autres produits.", carrefourOffer3, BoxType.NORMAL_BOX, BoxStatus.AVAILABLE, Category.DAIRY_PRODUCTS, product4);
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


            User livreur1 = createDeliveryBoy("Rachid", "Bouazza", "livreur1@foodeals.ma", "0612345678");
            User livreur2 = createDeliveryBoy("Youssef", "Haddad", "livreur2@foodeals.ma", "0612345679");

// Affecter deliveries aux orders
            Delivery delivery1 = createDeliveryForOrder(order1, livreur1, DeliveryStatus.ASSIGNED);
            Delivery delivery2 = createDeliveryForOrder(order2, livreur2, DeliveryStatus.ASSIGNED);
            Delivery delivery3 = createDeliveryForOrder(order3, livreur1, DeliveryStatus.ASSIGNED);

            order1.setDelivery(delivery1);
            order2.setDelivery(delivery2);
            order3.setDelivery(delivery3);
            orderRepository.save(order1);
            orderRepository.save(order2);
            orderRepository.save(order3);


            /**
             * Création hotel
             */

            Activity activityHotel = createActivity("Hôtels");
            User hotelManager = createUser("Ismail", "Ben Mabrouk", "ismail.mabrouk@golden-tolip.ma", "0650123456");
            User subEntityHotelManager = createUser("Mourad", "Ramhi", "mourad.ramhi@golden-tolip.ma", "0650765432");

            Address hotelMainAddress = createAddress("123 Rue Golden St", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
            OrganizationEntity goldenTolip = createOrganizationEntity("Golden-Tolip", activityHotel, hotelMainAddress, hotelManager);

            Address subEntityHotelAddress = createAddress("12  Rue Golden St Maaref", "Casablanca", "20000", "Morocco", "Casablanca-Settat");

            List<SubEntityDomain> domainsHotel = new ArrayList<>();
            Optional<SubEntityDomain> domainHotel = subEntityDomainRepository.findByName("Hôtels");
            domainsHotel.add(domainHotel.get());

            goldenTolipCasa = createSubEntity("Golden Tolip Casa", goldenTolip, subEntityHotelManager, activityHotel, subEntityHotelAddress, 400, false, 3.8f, domainsHotel);


            /**
             * Création restaurant
             */

            Activity activityRestaurant = createActivity("Restaurants");
            User restautantManager = createUser("Chafik", "Jarraya", "chafik.jarraya@kfc.ma", "0650123456");
            User subEntityRestaurantManager = createUser("Salim", "El iamani", "salim.eliamani@kfc.ma", "0650765432");

            Address restautantMainAddress = createAddress("123 Mohamed V St", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
            OrganizationEntity kfc = createOrganizationEntity("KFC", activityRestaurant, restautantMainAddress, restautantManager);

            Address subEntityRestaurantAddress = createAddress("12  Rue Ibnou Sina Maaref", "Casablanca", "20000", "Morocco", "Casablanca-Settat");

            List<SubEntityDomain> domainsRestaurant = new ArrayList<>();
            Optional<SubEntityDomain> domainRestaurant = subEntityDomainRepository.findByName("Restaurants");
            domainsRestaurant.add(domainRestaurant.get());

            kfcCasa = createSubEntity("KFC Casa", kfc, subEntityRestaurantManager, activityRestaurant, subEntityRestaurantAddress, 400, true, 4.8F, domainsRestaurant);


            Offer kfcOffer1 = createOffer(kfcCasa, new BigDecimal("129.99"), new BigDecimal("89.99"), 8, 3.3f);
            createOpenTimes(kfcOffer1);
            Offer kfcOffer2 = createOffer(kfcCasa, new BigDecimal("139.99"), new BigDecimal("89.99"), 10, 5f);
            createOpenTimes(kfcOffer2);

            Product productKfc1 = createProduct(kfcCasa, "Tinders", "Tinders Kabab.", new BigDecimal("5.99"), "Restaurants", 20);
            Product productKfc2 = createProduct(kfcCasa, "Chicken wings", "Box chings wings.", new BigDecimal("1.99"), "Restaurants", 10);
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
            subEntityIndustryManager = createUser("Mounir", "Ben Salha", "mounir.bansalha@delice.ma", "0650865432");
        }


        Address industryMainAddress = createAddress("123 Charles Egaul ", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
        OrganizationEntity delice = createOrganizationEntity("Delice", activityIndustry, industryMainAddress, industryManager);

        Address subEntityIndustryAddress = createAddress("Qaurtie industriel Casa", "Casablanca", "20000", "Morocco", "Casablanca-Settat");

        List<SubEntityDomain> domainsIndustry = new ArrayList<>();
        Optional<SubEntityDomain> domainIndustry = subEntityDomainRepository.findByName("Industriels");
        domainsIndustry.add(domainIndustry.get());

        SubEntity deliceCasa = createSubEntity("Delice Casa", delice, subEntityIndustryManager, activityIndustry, subEntityIndustryAddress, 400, true, 4.8F, domainsIndustry);


        /**
         * Création agriculture         */

        Activity activityAgriculture = createActivity("Agricultures");
        User agricultureManager = createUser("Samir", "Hamdani", "samir.hamdani@zalar-holding.ma", "0658123456");
        User subEntityAgriculureManager = createUser("Wafa", "Moutawakil", "wafa.moutawakil@zalar-holding.ma", "0659865432");

        Address agricultureMainAddress = createAddress("15 rue agriculture ", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
        OrganizationEntity zalar = createOrganizationEntity("Zalar Holding", activityAgriculture, agricultureMainAddress, agricultureManager);

        Address subEntityAgrocultureAddress = createAddress("Qaurtie agriculture Casa", "Casablanca", "20000", "Morocco", "Casablanca-Settat");

        List<SubEntityDomain> domainsAgriculture = new ArrayList<>();
        Optional<SubEntityDomain> domainAgriculture = subEntityDomainRepository.findByName("Agricultures");
        domainsAgriculture.add(domainAgriculture.get());

        SubEntity zalarCasa = createSubEntity("Zalar Holding Casa", delice, subEntityAgriculureManager, activityAgriculture, subEntityAgrocultureAddress, 400, true, 4.8F, domainsAgriculture);


        // OFFERS & DEALS POUR HÔTEL (Golden Tolip)
        Offer hotelOffer1 = createOffer(goldenTolipCasa, new BigDecimal("399.00"), new BigDecimal("299.00"), 12, 4.0f);
        createOpenTimes(hotelOffer1);

        Product hotelProduct1 = createProduct(goldenTolipCasa, "Nuitée Standard", "Chambre standard avec petit déjeuner", new BigDecimal("299.00"), "Hôtels", 10);
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

        // Coupon qui expire demain (after now)
        createCoupon(kfcCasa, "CARREFOUR10", 10f, new Date(System.currentTimeMillis() + 86400000L), true); // Enabled, expire demain

// Coupon qui a expiré hier (before now)
        createCoupon(kfcCasa, "CARREFOUR20", 20f, new Date(System.currentTimeMillis() - 86400000L), true);


        createBanner("https://cdn.monsite.com/banner1.jpg", "https://promo.monsite.com");

        createBanner("https://cdn.monsite.com/banner2.jpg", "https://offre.monsite.com");

        // Ajouter favoris à un client
        User client = userRepository.findByEmail("mohamed.benibrahim@example.com").orElse(null);
        if (client != null) {
            // Favoris SubEntities
            List<SubEntity> favorisSubEntities = new ArrayList<>();
            favorisSubEntities.add(kfcCasa);
            favorisSubEntities.add(goldenTolipCasa);
            client.setFavorisSubEntities(favorisSubEntities);

            // Favoris Products
            List<Product> favorisProducts = new ArrayList<>();
            favorisProducts.add(hotelProduct1);  // Pommes Bio
            client.setFavorisProducts(favorisProducts);

            userRepository.save(client);
            System.out.println("✅ Favoris ajoutés pour " + client.getEmail());
        }

        // ✅ Seed Donations
        User donor = userRepository.findByEmail("mohamed.benibrahim@example.com").orElse(null);
        if (donor != null && kfcCasa != null) {
            createDonation(donor, kfcCasa, 50.0, false, "Soutien aux familles");
            createDonation(donor, goldenTolipCasa, 100.0, true, "Aide aux étudiants");
            createDonation(donor, deliceCasa, 200.0, false, "Soutien médical");
            System.out.println("✅ Donations seedées pour " + donor.getEmail());
        }

        if (client != null) {
            createRating(client, agricultureProduct1, 5, "Excellent produit, très frais !");
            createRating(client, hotelProduct1, 3, "Bon produit mais un peu cher.");
            createRating(client, agricultureProduct1, 4, "Très bon goût, je recommande.");
            System.out.println("✅ Ratings ajoutés pour " + client.getEmail());
        } else {
            System.out.println("⚠️ Aucun utilisateur trouvé pour mohamed.benibrahim@example.com");
        }

        if (client != null) {
            SupportTicket ticket1 = new SupportTicket();
            ticket1.setUser(client);
            ticket1.setSubject("Problème de paiement");
            ticket1.setMessage("Impossible de payer avec ma carte.");
            ticket1.setCategory("Paiement");
            ticket1.setStatus("OPEN");

            supportTicketRepository.save(ticket1);

            SupportTicket ticket2 = new SupportTicket();
            ticket2.setUser(client);
            ticket2.setSubject("Erreur application");
            ticket2.setMessage("L'application plante au démarrage.");
            ticket2.setCategory("Technique");
            ticket2.setStatus("OPEN");

            supportTicketRepository.save(ticket2);

            System.out.println("✅ Support Tickets créés pour " + client.getEmail());
        }

        if (client != null) {
            BusinessRecommendation rec1 = new BusinessRecommendation();
            rec1.setBusinessName("Chez Pierre");
            rec1.setAddress("123 Rue de Paris, 75001 Paris");
            rec1.setCategory("Restaurant");
            rec1.setDescription("Un excellent restaurant français");

            businessRecommendationRepository.save(rec1);

            BusinessRecommendation rec2 = new BusinessRecommendation();
            rec2.setBusinessName("Librairie Le Savoir");
            rec2.setAddress("45 Avenue Hassan II, Casablanca");
            rec2.setCategory("Librairie");
            rec2.setDescription("Livres et papeterie de qualité");

            businessRecommendationRepository.save(rec2);

            System.out.println("✅ Business Recommendations créées");
        }

        if (client != null) {
            Referral ref1 = new Referral();
            ref1.setSender(client);
            ref1.setEmail("friend1@example.com");
            ref1.setSuccessful(true);
            ref1.setReward(10.0);
            referralRepository.save(ref1);

            Referral ref2 = new Referral();
            ref2.setSender(client);
            ref2.setEmail("friend2@example.com");
            ref2.setSuccessful(false);
            ref2.setReward(0.0);

            referralRepository.save(ref2);

            Referral ref3 = new Referral();
            ref3.setSender(client);
            ref3.setEmail("friend3@example.com");
            ref3.setSuccessful(true);
            ref3.setReward(15.0);

            referralRepository.save(ref3);

            System.out.println("✅ Referrals créés pour " + client.getEmail());
        }

        if (client != null) {
            Referral inv1 = new Referral();
            inv1.setSender(client);
            inv1.setEmail("invite1@example.com");
            inv1.setSuccessful(false);
            inv1.setReward(0.0);

            referralRepository.save(inv1);

            Referral inv2 = new Referral();
            inv2.setSender(client);
            inv2.setEmail("invite2@example.com");
            inv2.setSuccessful(false);
            inv2.setReward(0.0);

            referralRepository.save(inv2);

            System.out.println("✅ Sample referral invitations créées pour " + client.getEmail());
        }

        createPaymentMethod("CARD");
        createPaymentMethod("CASH");

        // ✅ Seed Delivery Options
        createDeliveryOption("HOME", "Home Delivery", 20.0, "MAD", "30-45 min");
        createDeliveryOption("PICKUP", "Pickup Point", 0.0, "MAD", "Ready in 15 min");
        addOtherAddressToUser(client, "Rue des Fleurs 45", "Casablanca", "20100", "Morocco", "Casablanca-Settat");
        addOtherAddressToUser(client, "Résidence Oasis, Maarif", "Casablanca", "20200", "Morocco", "Casablanca-Settat");

        // Dans ton OrganizationSeeder (à la fin du run par ex.)
        if (client != null) {
            // Création d’un panier (Cart)
            Cart cart = new Cart();
            cart.setUserId(client.getId());
            cart = cartRepository.save(cart);

            // Ajout d’items dans le panier
            // 1. Avec un Deal
            CartItem cartItemDeal = new CartItem(cart, agricultureDeal1, 2, ModalityType.DELIVERY);
            cartItemDeal.setCart(cart);
            cartItemDeal.setSubEntity(goldenTolipCasa);
            cartItemDeal = cartItemRepository.save(cartItemDeal);

            // 2. Avec une Box

            CartItem cartItemBox = new CartItem(cart, box2, 1, ModalityType.PICKUP);
            cartItemBox.setCart(cart);
            cartItemBox.setSubEntity(goldenTolipCasa);
            cartItemBox = cartItemRepository.save(cartItemBox);

            // 3. Avec un Product (direct)
            CartItem cartItemProduct = new CartItem(cart, product2, 3, ModalityType.AT_PLACE);
            cartItemProduct.setCart(cart);

            cartItemProduct.setSubEntity(goldenTolipCasa);
            cartItemProduct = cartItemRepository.save(cartItemProduct);

            // Attacher les items au panier
            //  cart.setItems(List.of(cartItemDeal, cartItemBox, cartItemProduct));
            cartRepository.save(cart);

            System.out.println("✅ Cart créé avec des items pour " + client.getEmail());
        }

        // ==========================
        // ✅ SEED DES DLC (Dates Limite de Consommation)
        // ==========================
        System.out.println("⏳ Création des DLC pour tests...");

// On suppose que tu as une entité  reliée à Product
// Exemple : Dlc(product, expiryDate, initialStock)
        Date now = new Date();
        Calendar cal = Calendar.getInstance();

// DLC expirée hier
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date expiredDate = cal.getTime();

// DLC qui expire dans 3 jours
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, 3);
        Date soonDate = cal.getTime();
        // DLC qui expire dans 10 jours
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, 10);
        Date laterDate = cal.getTime();

// On prend quelques produits existants du seeder
        List<Product> existingProducts = productRepository.findAll();
        if (!existingProducts.isEmpty()) {
            Product productA = existingProducts.get(0);
            Product productB = existingProducts.get(1);
            Product productC = existingProducts.get(2);

            Dlc dlcExpired = new Dlc(productA, expiredDate, 10);
            Dlc dlcSoon = new Dlc(productB, soonDate, 15);
            Dlc dlcLater = new Dlc(productC, laterDate, 30);

            dlcRepository.saveAll(List.of(dlcExpired, dlcSoon, dlcLater));
            System.out.println("✅ DLC créés pour produits : " + productA.getName() + ", " + productB.getName() + ", " + productC.getName());
        } else {
            System.out.println("⚠️ Aucun produit trouvé pour créer des DLC.");
        }

    }

    private Rating createRating(User user, Product product, int ratingValue, String comment) {
        Rating rating = new Rating();
        rating.setUser(user);
        rating.setProduct(product);
        rating.setRating(ratingValue);
        rating.setComment(comment);

        // ⚠️ si tu veux aussi garder le store/subEntity
        if (product.getSubEntity() != null) {
            rating.setSubEntity(product.getSubEntity());
        }

        return ratingRepository.save(rating);
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
    private Address createAddress(String address, String city, String zip, String country, String region) {
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


    private void addOtherAddressToUser(User user, String address, String city, String zip, String country, String region) {
        Address addr = createAddress(address, city, zip, country, region);
        user.getOtherAddresses().add(addr);
        userRepository.save(user);
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
    private Product createProduct(SubEntity subEntity, String name, String description, BigDecimal price, String category, Integer stock) {
        Product product = new Product();
        product.setName(name);
        product.setProductImagePath("/images/" + name.toLowerCase().replace(" ", "-") + "-avatar.png");
        product.setDescription(description);
        product.setPrice(new Price(price != null ? price : BigDecimal.valueOf(10), Currency.getInstance("MAD")));
        product.setSubEntity(subEntity);

        ProductCategory productCategory = productCategoryRepository.findByName(category).orElse(null);
        product.setCategory(productCategory);
        product.setStock(stock != null ? stock : 1);
        product.setBrand("Generic Brand");

        // ✅ Génération d’un barcode si absent
        String barcode = "BAR" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        product.setBarcode(barcode);

        // ✅ Sauvegarde du produit
        Product savedProduct = productRepository.save(product);

        // ✅ Création d’une date d’expiration réaliste
        Random random = new Random();
        int randomDays = random.nextInt(20) + 5; // entre 5 et 25 jours
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, randomDays);
        Date randomExpiryDate = cal.getTime();
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
    private Offer createOffer(SubEntity subEntity, BigDecimal salePrice, BigDecimal price, Integer numberOfFeedback, Float numberOfStars) {
        Offer offer = new Offer();
        offer.setReduction(25);
        offer.setType("flash");
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
        boolean isFeatured = ThreadLocalRandom.current().nextBoolean();
        deal.setFeatured(isFeatured);

        boolean isActive = ThreadLocalRandom.current().nextBoolean();
        deal.setActive(isActive);
        return dealRepository.save(deal);
    }

    // Méthode pour créer une box associée à un produit
    private Box createBoxWithOffer(String title, String description, Offer offer, BoxType type, BoxStatus status, Category category, Product product

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
        boolean isFeatured = ThreadLocalRandom.current().nextBoolean();
        box.setFeatured(isFeatured);

        boolean isActive = ThreadLocalRandom.current().nextBoolean();
        box.setActive(isActive);
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


    private Transaction createTransaction(String paymentId, String reference, String context, Price price, TransactionStatus status, TransactionType type, Order order) {
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
        return createTransaction(paymentId, reference, "Paiement effectué", price, TransactionStatus.COMPLETED, TransactionType.CASH, order);
    }


    private Donate createDonation(User user, SubEntity receiver, Double amount, boolean anonymous, String cause) {
        Donate donate = new Donate();
        donate.setUserDonor(user);
        donate.setReceiver(receiver.getOrganizationEntity());
        donate.setAmount(amount);
        donate.setIsAnonymous(anonymous);
        donate.setDonateStatus(DonateStatus.COMPLETED);
        donate.setDonationType(DonationType.ONE);
        donate.setDonateDelivryMethod(DonateDeliveryMethod.FOODEALS);
        donate.setReason(cause);
        return donateRepository.save(donate);
    }


    private void createBanner(String imageUrl, String link) {
        // Vérifie si la bannière existe déjà (pour éviter doublons)
        boolean exists = bannerRepository.findAll().stream().anyMatch(b -> b.getImageUrl().equals(imageUrl) && b.getLink().equals(link));

        if (!exists) {
            Banner banner = new Banner(imageUrl, link);
            bannerRepository.save(banner);
            System.out.println("✅ Banner créée : " + link);
        } else {
            System.out.println("ℹ️ Bannière déjà existante : " + link);
        }
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

    private User createDeliveryBoy(String firstName, String lastName, String email, String phone) {
        User deliveryBoy = new User();
        deliveryBoy.setName(new Name(firstName, lastName));
        deliveryBoy.setEmail(email);
        deliveryBoy.setPhone(phone);
        return userRepository.save(deliveryBoy);
    }

    private Delivery createDeliveryForOrder(Order order, User deliveryBoy, DeliveryStatus deliveryStatus) {
        Delivery delivery = new Delivery();
        delivery.setDeliveryBoy(deliveryBoy);
        delivery.setStatus(deliveryStatus);
        delivery.setOrder(order);
        delivery.setDuration(14l);

        // Position simulée
        DeliveryPosition position = new DeliveryPosition();
        position.setCoordinates(new Coordinates(33.5731F, -7.5898F)); // Casablanca exemple
        delivery.setDeliveryPosition(deliveryPositionRepository.save(position));

        delivery = deliveryRepository.save(delivery);

        // Création d’un TrackingStep initial
        TrackingStep step = new TrackingStep();
        step.setOrder(order);
        step.setStatus("ASSIGNED");
        step.setDescription("Commande assignée au livreur");
        step.setTimestamp(java.time.Instant.now());
        trackingStepRepository.save(step);

        return delivery;
    }

    private void createPaymentMethod(String label) {

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setLabel(label);

        paymentMethodRepository.save(paymentMethod);


    }

    private void createDeliveryOption(String type, String label, Double cost, String currency, String estimatedTime) {

        DeliveryOption option = new DeliveryOption();
        option.setLabel(label);
        option.setCost(cost);
        option.setCurrency(currency);
        option.setEstimatedTime(estimatedTime);
        deliveryOptionRepository.save(option);
        System.out.println("✅ DeliveryOption créée : " + type);

    }
}






