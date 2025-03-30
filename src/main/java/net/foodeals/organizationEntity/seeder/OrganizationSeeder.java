package net.foodeals.organizationEntity.seeder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
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
@Order(3)
public class OrganizationSeeder implements CommandLineRunner {

	private final OrganizationEntityRepository organizationEntityRepository;
	private final SubEntityRepository subEntityRepository;
	private final ActivityRepository activityRepository;
	private final UserRepository userRepository;
	private final CityRepository cityRepository;
	private final CountryRepository countryRepository;
	private final AddressRepository addressRepository;

	private final DealRepository dealRepository; // Le repository Deal
	private final BoxRepository boxRepository; // Le repository Box
	private final OfferRepository offerRepository; // Le repository Offer

	@Override
	public void run(String... args) throws Exception {
        if(Objects.isNull(organizationEntityRepository.findByName("Carrefour").get()))
        {
		// Création de l'activité "Supermarchés"
		Activity activity1 = Activity.create("Supermarchés");
		activityRepository.save(activity1);

		// Création du manager du partenaire
		User partnerManager = new User();
		partnerManager.setName(new Name("Ahmed", "Ben Ali"));
		partnerManager.setEmail("ahmed.ben.ali@carrefour.ma");
		partnerManager.setPhone("0650123456");
		userRepository.save(partnerManager);

		// Création du manager de la sous-entité
		User subEntityManager = new User();
		subEntityManager.setName(new Name("Sara", "El Fassi"));
		subEntityManager.setEmail("sara.elfassi@carrefourmarket.ma");
		subEntityManager.setPhone("0650765432");
		userRepository.save(subEntityManager);

		// Création de l'adresse pour l'OrganizationEntity
		Address address = new Address();
		address.setAddress("123 Carrefour St");
		address.setCity(cityRepository.findByName("Casablanca"));
		address.setZip("20000");
		address.setCountry(countryRepository.findByName("Maroc"));

		// Création de l'organizationEntity "Carrefour"
		OrganizationEntity carrefour = OrganizationEntity.builder().name("Carrefour")
				.avatarPath("/images/carrefour-avatar.png").coverPath("/images/carrefour-cover.png")
				.type(EntityType.PARTNER).mainActivity(activity1).address(address).commercialNumber("123456789")
				.users(new ArrayList<>(List.of(partnerManager))).build();
		organizationEntityRepository.save(carrefour);

	
		// Création de la sous-entité "Carrefour Market"
		SubEntity carrefourMarket = new SubEntity();
		carrefourMarket.setName("Carrefour Market");
		carrefourMarket.setAvatarPath("/images/carrefour-market-avatar.png");
		carrefourMarket.setCoverPath("/images/carrefour-market-cover.png");
		Activity restaurantActivity=createActivity("restaurant");
		carrefourMarket.setType(SubEntityType.PARTNER_SB);
		carrefourMarket.setActivities(List.of(restaurantActivity));
		carrefourMarket.setCoordinates(new Coordinates(33.5731F, -7.5898F));
		carrefourMarket.setManager(subEntityManager);
		carrefourMarket.setOrganizationEntity(carrefour);
		carrefourMarket.setSubEntityStatus(SubEntityStatus.ACTIVE);
		carrefourMarket.setActivities(new ArrayList<>(List.of(activity1)));

		// Création de l'adresse pour le "Carrefour Market"
		Address address2 = new Address();
		address2.setAddress("12 Carrefour St Maaref");
		address2.setCity(cityRepository.findByName("Casablanca"));
		address2.setZip("20000");
		address2.setCountry(countryRepository.findByName("Maroc"));

		carrefourMarket.setAddress(addressRepository.save(address2));
		subEntityRepository.save(carrefourMarket);

		// Ajout de la sous-entité à l'organizationEntity
		carrefour.getSubEntities().add(carrefourMarket);
		organizationEntityRepository.save(carrefour);

		// Création d'une offre (Offer) pour Carrefour Market
		Offer carrefourOffer1 = new Offer();
		carrefourOffer1.setSubEntity(carrefourMarket);
		carrefourOffer1.setPrice(new Price(new BigDecimal("49.99"), Currency.getInstance("MAD")));
		offerRepository.save(carrefourOffer1); // Enregistrement de l'offre

		// Création de Deal pour cette offre
		Deal carrefourDeal = new Deal();
		carrefourDeal.setTitle("Promo Carrefour Market");
		carrefourDeal.setDescription("Réduction sur une sélection de produits.");
		carrefourDeal.setPrice(new Price(new BigDecimal("49.99"), Currency.getInstance("MAD")));
		carrefourDeal.setDealStatus(DealStatus.AVAILABLE);
		carrefourDeal.setCategory(Category.FRUITS_AND_VEGETABLES);
		carrefourDeal.setPublishAs(PublishAs.SUPERMARKETS_HYPERMARKETS);
		carrefourDeal.setOffer(carrefourOffer1);
		dealRepository.save(carrefourDeal);

		// Création d'une offre (Offer) pour Carrefour Market
		Offer carrefourOffer2 = new Offer();
		carrefourOffer2.setPrice(new Price(new BigDecimal("49.99"), Currency.getInstance("MAD")));
		carrefourOffer2.setSubEntity(carrefourMarket);
		offerRepository.save(carrefourOffer2); // Enregistrement de l'offre

		// Création de Box associée à l'offre et au Deal
		Box carrefourBox = new Box(BoxType.NORMAL_BOX);
		carrefourBox.setTitle("Box Carrefour Market");
		carrefourBox.setDescription("Une box avec des produits au choix.");
		carrefourBox.setOffer(carrefourOffer2); // Associer la box à l'offre
		carrefourBox.setCategory(Category.DAIRY_PRODUCTS);
		carrefourBox.setPublishAs(PublishAs.SUPERMARKETS_HYPERMARKETS); // Type de publication
		carrefourBox.setBoxStatus(BoxStatus.AVAILABLE); // Statut de la box
		boxRepository.save(carrefourBox); // Enregistrement de la Box

		// Création d'une autre offre (Offer) avec une Box pour Carrefour
		Offer anotherOffer = new Offer();
		anotherOffer.setPrice(new Price(new BigDecimal("49.99"), Currency.getInstance("MAD")));
		anotherOffer.setSubEntity(carrefourMarket);
		offerRepository.save(anotherOffer); // Enregistrement de l'offre

		// Création de Deal pour cette autre offre
		Deal anotherDeal = new Deal();
		anotherDeal.setTitle("Promotion Noël Carrefour");
		anotherDeal.setDescription("Offre spéciale pour Noël");
		anotherDeal.setPrice(new Price(new BigDecimal("49.99"), Currency.getInstance("MAD")));
		anotherDeal.setDealStatus(DealStatus.AVAILABLE);
		anotherDeal.setCategory(Category.FROZEN_PRODUCTS);
		anotherDeal.setPublishAs(PublishAs.SUPERMARKETS_HYPERMARKETS);
		anotherDeal.setOffer(anotherOffer); // Associer l'offre à l'offre Deal
		dealRepository.save(anotherDeal); // Enregistrement du deal

		// Création de Box associée à cette autre offre
		Box anotherBox = new Box(BoxType.NORMAL_BOX);
		anotherBox.setTitle("Box Noël Carrefour");
		anotherBox.setDescription("Box de Noël avec produits variés.");
		anotherBox.setOffer(anotherOffer); // Associer la box à l'offre
		anotherBox.setCategory(Category.DAIRY_PRODUCTS);
		anotherBox.setPublishAs(PublishAs.SUPERMARKETS_HYPERMARKETS); // Type de publication
		anotherBox.setBoxStatus(BoxStatus.AVAILABLE); // Statut de la box
		boxRepository.save(anotherBox); // Enregistrement de la Box
        }
	}
	
	private Activity createActivity(String name) {
		Activity activity=new Activity();
		activity.setName(name);
		return activityRepository.save(activity);
	}
}
