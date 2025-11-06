package net.foodeals.organizationEntity.seeder;

import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.*;
import net.foodeals.core.domain.enums.*;
import net.foodeals.core.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
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
    private final DlcRepository dlcRepository;

    @Override
    public void run(String... args) throws Exception {

        // ===========================
        // ACTIVITIES
        // ===========================
        Activity activitySupermarket = createActivity("Supermarchés");
        Activity activityHotel = createActivity("Hôtels");
        Activity activityRestaurant = createActivity("Restaurants");
        Activity activityIndustry = createActivity("Industriels");
        Activity activityAgriculture = createActivity("Agricultures");

        // ===========================
        // USERS
        // ===========================
        User partnerManagerCarrefour = createUser("Ahmed", "Ben Ali", "ahmed.ben.ali@carrefour.ma", "0650123456");
        User subEntityManagerCarrefour = createUser("Sara", "El Fassi", "sara.elfassi@carrefourmarket.ma", "0650765432");
        User hotelManager = createUser("Ismail", "Ben Mabrouk", "ismail.mabrouk@golden-tolip.ma", "0650123456");
        User subEntityHotelManager = createUser("Mourad", "Ramhi", "mourad.ramhi@golden-tolip.ma", "0650765432");
        User restaurantManager = createUser("Chafik", "Jarraya", "chafik.jarraya@kfc.ma", "0650123456");
        User subEntityRestaurantManager = createUser("Salim", "El Iamani", "salim.eliamani@kfc.ma", "0650765432");
        User industryManager = createUser("Kamel", "Ltaief", "kamel.ltaiefa@delice.ma", "0650123456");
        User subEntityIndustryManager = createUser("Mounir", "Ben Salha", "mounir.bansalha@delice.ma", "0650865432");
        User agricultureManager = createUser("Samir", "Hamdani", "samir.hamdani@zalar-holding.ma", "0658123456");
        User subEntityAgricultureManager = createUser("Wafa", "Moutawakil", "wafa.moutawakil@zalar-holding.ma", "0659865432");

        // ===========================
        // ADDRESSES
        // ===========================
        Address carrefourAddress = createAddress("123 Carrefour St", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
        Address carrefourMarketAddress = createAddress("12 Carrefour St Maaref", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
        Address hotelAddress = createAddress("123 Rue Golden St", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
        Address hotelSubEntityAddress = createAddress("12 Rue Golden St Maaref", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
        Address restaurantAddress = createAddress("123 Mohamed V St", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
        Address restaurantSubEntityAddress = createAddress("12 Rue Ibnou Sina Maaref", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
        Address industryAddress = createAddress("123 Charles Egaul", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
        Address industrySubEntityAddress = createAddress("Quartier industriel Casa", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
        Address agricultureAddress = createAddress("15 rue agriculture", "Casablanca", "20000", "Morocco", "Casablanca-Settat");
        Address agricultureSubEntityAddress = createAddress("Quartier agriculture Casa", "Casablanca", "20000", "Morocco", "Casablanca-Settat");

        // ===========================
        // ORGANIZATIONS
        // ===========================
        OrganizationEntity carrefour = createOrganizationEntity("Carrefour", activitySupermarket, carrefourAddress, partnerManagerCarrefour);
        OrganizationEntity goldenTolip = createOrganizationEntity("Golden-Tolip", activityHotel, hotelAddress, hotelManager);
        OrganizationEntity kfc = createOrganizationEntity("KFC", activityRestaurant, restaurantAddress, restaurantManager);
        OrganizationEntity delice = createOrganizationEntity("Delice", activityIndustry, industryAddress, industryManager);
        OrganizationEntity zalar = createOrganizationEntity("Zalar Holding", activityAgriculture, agricultureAddress, agricultureManager);

        // ===========================
        // SUBENTITIES
        // ===========================
        SubEntityDomain domainSupermarket = createDomain("Supermarchés");
        SubEntityDomain domainSuperette = createDomain("Superettes");
        SubEntityDomain domainHotel = createDomain("Hôtels");
        SubEntityDomain domainRestaurant = createDomain("Restaurants");
        SubEntityDomain domainIndustry = createDomain("Industriels");
        SubEntityDomain domainAgriculture = createDomain("Agricultures");

        SubEntity carrefourMarket = createSubEntity("Carrefour Market", carrefour, subEntityManagerCarrefour, activitySupermarket,
                carrefourMarketAddress, 39, true, 4.5f, Arrays.asList(domainSupermarket, domainSuperette));
        SubEntity goldenTolipCasa = createSubEntity("Golden Tolip Casa", goldenTolip, subEntityHotelManager, activityHotel,
                hotelSubEntityAddress, 400, false, 3.8f, Collections.singletonList(domainHotel));
        SubEntity kfcCasa = createSubEntity("KFC Casa", kfc, subEntityRestaurantManager, activityRestaurant,
                restaurantSubEntityAddress, 400, true, 4.8f, Collections.singletonList(domainRestaurant));
        SubEntity deliceCasa = createSubEntity("Delice Casa", delice, subEntityIndustryManager, activityIndustry,
                industrySubEntityAddress, 400, true, 4.8f, Collections.singletonList(domainIndustry));
        SubEntity zalarCasa = createSubEntity("Zalar Holding Casa", zalar, subEntityAgricultureManager, activityAgriculture,
                agricultureSubEntityAddress, 400, true, 4.8f, Collections.singletonList(domainAgriculture));

        // ===========================
        // PRODUCTS
        // ===========================
        Product pommesBio = createProduct(carrefourMarket, "Pommes Bio", "Pommes fraîches et biologiques.", new BigDecimal("5.99"), "Supermarchés", 20);
        Product laitEntier = createProduct(carrefourMarket, "Lait entier", "Bouteille de lait entier 1L.", new BigDecimal("1.99"), "Supermarchés", 10);
        Product buchenoel = createProduct(carrefourMarket, "Buche de Noël", "Délicieux gâteau de Noël.", new BigDecimal("15.99"), "Supermarchés", 6);
        Product laitFraise = createProduct(carrefourMarket, "Lait entier fraise", "Lait avec fraise.", new BigDecimal("5.99"), "Supermarchés", 20);

        Product nuiteeStandard = createProduct(goldenTolipCasa, "Nuitée Standard", "Chambre standard avec petit déjeuner", new BigDecimal("299.00"), "Hôtels", 10);
        Product tinders = createProduct(kfcCasa, "Tinders", "Tinders Kabab.", new BigDecimal("5.99"), "Restaurants", 20);
        Product chickenWings = createProduct(kfcCasa, "Chicken Wings", "Box wings.", new BigDecimal("1.99"), "Restaurants", 10);
        Product packLait = createProduct(deliceCasa, "Pack industriel lait", "Lot de lait pour industrie", new BigDecimal("1999.00"), "Industriels", 5);
        Product packPoulets = createProduct(zalarCasa, "Pack Poulets Fermiers", "Volaille élevée en plein air", new BigDecimal("999.00"), "Agricultures", 15);

        // ===========================
        // OFFERS
        // ===========================
        Offer carrefourOffer1 = createOffer(carrefourMarket, new BigDecimal("29.99"), new BigDecimal("49.99"), 32, 4.2f);
        Offer carrefourOffer2 = createOffer(carrefourMarket, new BigDecimal("29.99"), new BigDecimal("59.99"), 20, 3.2f);
        Offer carrefourOffer3 = createOffer(carrefourMarket, new BigDecimal("69.99"), new BigDecimal("79.99"), 20, 3.2f);

        Offer hotelOffer1 = createOffer(goldenTolipCasa, new BigDecimal("399.00"), new BigDecimal("299.00"), 12, 4.0f);
        Offer kfcOffer1 = createOffer(kfcCasa, new BigDecimal("129.99"), new BigDecimal("89.99"), 8, 3.3f);
        Offer kfcOffer2 = createOffer(kfcCasa, new BigDecimal("139.99"), new BigDecimal("89.99"), 10, 5f);
        Offer industryOffer1 = createOffer(deliceCasa, new BigDecimal("2500.00"), new BigDecimal("1999.00"), 7, 4f);
        Offer agricultureOffer1 = createOffer(zalarCasa, new BigDecimal("1499.00"), new BigDecimal("999.00"), 6, 4.9f);

        // ===========================
        // DEALS
        // ===========================
        Deal pommesDeal = createDealWithOfferAndProduct("Promo Carrefour Market", "Réduction pommes bio.", carrefourOffer1, 1, DealStatus.AVAILABLE, Category.FRUITS_AND_VEGETABLES, pommesBio);
        Deal noelDeal = createDealWithOfferAndProduct("Promotion Noël Carrefour", "Offre spéciale sur le gâteau de Noël.", carrefourOffer2, 2, DealStatus.AVAILABLE, Category.FROZEN_PRODUCTS, buchenoel);
        Deal hotelDeal1 = createDealWithOfferAndProduct("Offre Nuitée", "Promotion sur chambre avec PDJ.", hotelOffer1, 1, DealStatus.AVAILABLE, Category.WHOLESALER_DAIRY_PRODUCTS, nuiteeStandard);
        Deal dealTinders = createDealWithOfferAndProduct("Deux tinders achetés un gratuit", "Offre spéciale étudiant.", kfcOffer2, 2, DealStatus.AVAILABLE, Category.FROZEN_PRODUCTS, tinders);
        Deal dealWings = createDealWithOfferAndProduct("23 wings magics", "Réduction spéciale.", kfcOffer1, 1, DealStatus.AVAILABLE, Category.FRUITS_AND_VEGETABLES, chickenWings);
        Deal industryDeal1 = createDealWithOfferAndProduct("Pack Lait Industrie", "Remise sur gros volume lait", industryOffer1, 1, DealStatus.AVAILABLE, Category.DAIRY_PRODUCTS, packLait);
        Deal agricultureDeal1 = createDealWithOfferAndProduct("Offre Poulets Bio", "Réduction sur élevage durable", agricultureOffer1, 1, DealStatus.AVAILABLE, Category.WHOLESALER_DAIRY_PRODUCTS, packPoulets);

        // ===========================
        // BOXES
        // ===========================
        Box box1 = createBoxWithOffer("Box Carrefour Market", "Box avec lait et autres produits.", carrefourOffer1, BoxType.NORMAL_BOX, BoxStatus.AVAILABLE, Category.DAIRY_PRODUCTS, laitEntier);
        Box box2 = createBoxWithOffer("Box Carrefour Market 2", "Box avec lait Fraises et autres produits.", carrefourOffer3, BoxType.NORMAL_BOX, BoxStatus.AVAILABLE, Category.DAIRY_PRODUCTS, laitFraise);
        Box box3 = createBoxWithOffer("Box Spéciale Noël", "Box garnie pour Noël avec gâteaux.", carrefourOffer2, BoxType.NORMAL_BOX, BoxStatus.AVAILABLE, Category.FROZEN_PRODUCTS, buchenoel);

        // ===========================
        // SUPPLEMENTS
        // ===========================
        createSupplement("Lait", SupplementCategory.SUPPLEMENTS, noelDeal, box1);
        createSupplement("Coca", SupplementCategory.DRINK, dealWings, null);
        createSupplement("Fanta", SupplementCategory.DRINK, dealWings, null);
        createSupplement("Sauce mayonnaise", SupplementCategory.SAUCE, dealTinders, null);
        createSupplement("Sauce algerienne", SupplementCategory.SAUCE, dealTinders, null);

        // ===========================
        // Coupons
        // ===========================
        createCoupon(kfcCasa, "CARREFOUR10",  new Date(System.currentTimeMillis() + 86400000L), true);
        createCoupon(kfcCasa, "CARREFOUR20", new Date(System.currentTimeMillis() - 86400000L), true);

        // ===========================
        // Banners
        // ===========================
        createBanner("https://cdn.monsite.com/banner1.jpg", "https://promo.monsite.com");
        createBanner("https://cdn.monsite.com/banner2.jpg", "https://offre.monsite.com");

        // ===========================
        // DLC
        // ===========================
        createDlcForProduct(pommesBio, -1, 10); // Expirée
        createDlcForProduct(laitEntier, 3, 15);
        createDlcForProduct(buchenoel, 10, 30);

        System.out.println("✅ Seeder complet terminé avec toutes les entités créées explicitement !");
    }

    // ==================================
    // Méthodes utilitaires explicit
    // ==================================
    private Activity createActivity(String name) {
        Activity activity = new Activity();
        activity.setName(name);
        return activityRepository.save(activity);
    }

    private User createUser(String firstName, String lastName, String email, String phone) {
        User user = new User();
        user.setName(new Name(firstName, lastName));
        user.setEmail(email);
        user.setPhone(phone);
        return userRepository.save(user);
    }

    private Address createAddress(String address, String city, String zip, String country, String region) {
        Address addr = new Address();
        addr.setAddress(address);
        addr.setExtraAddress("Quartier " + city);
        addr.setZip(zip);
        addr.setCoordinates(new Coordinates(33.5731F, -7.5898F));
        addr.setCity(cityRepository.findByName(city));
        addr.setCountry(countryRepository.findByName(country));
        addr.setRegion(regionRepository.findByName(region));
        addr.setAddressType(AddressType.HOME);
        addr.setContactName("Service Client " + city);
        addr.setContactEmail("contact@" + city.toLowerCase() + ".ma");
        addr.setContactPhone("+212600000000");
        addr.setIdMapCity("city.12345");
        addr.setIdMapCountry("country.12345");
        addr.setIdMapRegion("region.12345");
        return addressRepository.save(addr);
    }

    private OrganizationEntity createOrganizationEntity(String name, Activity activity, Address address, User manager) {
        OrganizationEntity org = OrganizationEntity.builder()
                .name(name)
                .avatarPath("/images/" + name.toLowerCase() + "-avatar.png")
                .coverPath("/images/" + name.toLowerCase() + "-cover.png")
                .type(EntityType.PARTNER)
                .mainActivity(activity)
                .address(address)
                .commercialNumber("123456789")
                .users(Collections.singletonList(manager))
                .build();
        return organizationEntityRepository.save(org);
    }

    private SubEntityDomain createDomain(String name) {
        SubEntityDomain domain = new SubEntityDomain();
        domain.setName(name);
        return subEntityDomainRepository.save(domain);
    }

    private SubEntity createSubEntity(String name, OrganizationEntity org, User manager, Activity activity, Address address, int likes, boolean feeDelivered, float stars, List<SubEntityDomain> domains) {
        SubEntity sub = new SubEntity();
        sub.setName(name);
        sub.setAvatarPath("/images/" + name.toLowerCase().replace(" ", "-") + "-avatar.png");
        sub.setCoverPath("/images/" + name.toLowerCase().replace(" ", "-") + "-cover.png");
        sub.setType(SubEntityType.PARTNER_SB);
        sub.setOrganizationEntity(org);
        sub.setManager(manager);
        sub.setActivities(Collections.singletonList(activity));
        sub.setAddress(address);
        sub.setNumberOfLikes(likes);
        sub.setFeeDelivered(feeDelivered);
        sub.setNumberOfStars(stars);
        sub.setSubEntityDomains(domains);
        sub.setModalityTypes(Arrays.asList(ModalityType.AT_PLACE, ModalityType.DELIVERY, ModalityType.PICKUP));
        return subEntityRepository.save(sub);
    }

    private OpenTime createOpenTime() {
        OpenTime openTime = new OpenTime();
        openTime.setFrom(DayOfWeek.MONDAY.name());
        openTime.setTo(DayOfWeek.FRIDAY.name());
        return openTimeRepository.save(openTime);
    }

    private Product createProduct(SubEntity subEntity, String name, String description, BigDecimal price, String category, int stock) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(new Price(price ,Currency.getInstance("MAD")));
        product.setSubEntity(subEntity);
        product.setStock(stock);
        product.setCategory(productCategoryRepository.findByName(category).get());
        return productRepository.save(product);
    }

    private Offer createOffer(SubEntity subEntity, BigDecimal price, BigDecimal oldPrice, int quantity, float rating) {
        Offer offer = new Offer();
        offer.setSubEntity(subEntity);
        offer.setPrice(new Price(price,Currency.getInstance("MAD")));
        offer.setSalePrice(new Price(oldPrice,Currency.getInstance("MAD")));
        return offerRepository.save(offer);
    }

    private Deal createDealWithOfferAndProduct(String name, String description, Offer offer, int quantity, DealStatus status, Category category, Product product) {
        Deal deal = new Deal();
        deal.setTitle(name);
        deal.setDescription(description);
        deal.setOffer(offer);
        deal.setQuantity(quantity);
        deal.setDealStatus(status);
        deal.setCategory(category);
        deal.setProduct(product);
        deal.setPublishAs(PublishAs.BAKERIES_PASTRIES);
        return dealRepository.save(deal);
    }

    private Box createBoxWithOffer(String name, String description, Offer offer, BoxType type, BoxStatus status, Category category, Product product) {
        Box box = new Box();
        box.setTitle(name);
        box.setDescription(description);
        box.setOffer(offer);
        box.setType(type);
        box.setBoxStatus(status);
        box.setCategory(category);
        box.setPublishAs(PublishAs.BAKERIES_PASTRIES);
        box.setProducts(List.of(product));
        return boxRepository.save(box);
    }

    private Supplement createSupplement(String name, SupplementCategory category, Deal deal, Box box) {
        Supplement supplement = new Supplement();
        supplement.setName(name);
        supplement.setSupplementCategory(category);
        supplement.setDeal(deal);
        supplement.setBox(box);
        return supplementRepository.save(supplement);
    }

    private Coupon createCoupon(SubEntity subEntity, String code, Date expirationDate, boolean active) {
        Coupon coupon = new Coupon();
        coupon.setCode(code);
        coupon.setSubEntity(subEntity);
        coupon.setEndsAt(expirationDate);
        coupon.setIsEnabled(active);
        return couponRepository.save(coupon);
    }

    private Banner createBanner(String imageUrl, String link) {
        Banner banner = new Banner();
        banner.setImageUrl(imageUrl);
        banner.setLink(link);
        return bannerRepository.save(banner);
    }

    private Dlc createDlcForProduct(Product product, int daysValidity, int quantity) {
        Dlc dlc = new Dlc();
        dlc.setProduct(product);
        dlc.setQuantity(quantity);
        if (daysValidity > 0) {
            dlc.setExpiryDate(new Date(System.currentTimeMillis() + daysValidity * 24L * 60 * 60 * 1000));
        } else {
            dlc.setExpiryDate(new Date(System.currentTimeMillis() - 24L * 60 * 60 * 1000)); // Expirée
        }
        return dlcRepository.save(dlc);
    }
}
