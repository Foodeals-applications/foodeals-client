package net.foodeals.offer.application.services.impl;

import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.application.dtos.requests.OfferRequest;
import net.foodeals.offer.application.services.OfferService;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;
import net.foodeals.organizationEntity.domain.repositories.OrganizationEntityRepository;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.organizationEntity.domain.repositories.SubEntityRepository;
import net.foodeals.common.valueOjects.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private BoxRepository boxRepository;

	@Autowired
	private OrganizationEntityRepository organizationEntityRepository;

	@Autowired
	private SubEntityRepository subEntityRepository;

	// Rayon en kilomètres
	private static final double EARTH_RADIUS = 6371.0;

	// Fonction pour calculer la distance entre deux points en utilisant la formule
	// de Haversine
	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS * c; // Retourne la distance en kilomètres
	}

	// Fonction pour obtenir la liste des offres et boxes les plus proches
	public Map<String, Object> getNears(double userLat, double userLon, double radius) {
		Map<String, Object> resultMap = new HashMap<>();

		// Récupérer les sous-entités liées à chaque type de partenaire
		List<SubEntity> restaurants = subEntityRepository.findByDomaineName("Restaurants");
		List<SubEntity> superMarches = subEntityRepository.findByDomaineName("Supermarchés");
		List<SubEntity> superettes = subEntityRepository.findByDomaineName("Superettes");
		List<SubEntity> hyperMarches = subEntityRepository.findByDomaineName("Hypermarchés");
		List<SubEntity> boulangeries = subEntityRepository.findByDomaineName("Pâtisseries" );
		List<SubEntity> hotels = subEntityRepository.findByDomaineName("Hôtels");
		List<SubEntity> agricultures = subEntityRepository.findByDomaineName("Agricultures");
		List<SubEntity> industries = subEntityRepository.findByDomaineName("Industriels");

		// Filtrer les sous-entités en fonction de la distance
		resultMap.put("newDeals", getNewDeals(userLat, userLon, radius));
		resultMap.put("boxes", getBoxes(userLat, userLon, radius));
		resultMap.put("restaurants", getPartners(userLat, userLon, radius, restaurants));
		resultMap.put("superMarches", getPartners(userLat, userLon, radius, superMarches));
		resultMap.put("superettes", getPartners(userLat, userLon, radius, superettes));
		resultMap.put("hyperMarches", getPartners(userLat, userLon, radius, hyperMarches));
		resultMap.put("boulangeries", getPartners(userLat, userLon, radius, boulangeries));
		resultMap.put("hotels", getHotels(userLat, userLon, radius, hotels));
		resultMap.put("agricultures", getPartners(userLat, userLon, radius, agricultures));
		resultMap.put("industries", getPartners(userLat, userLon, radius, industries));

		return resultMap;
	}

	// Filtrer les deals en fonction de la distance
	private List<Map<String, Object>> getNewDeals(double userLat, double userLon, double radius) {
		List<Deal> deals = dealRepository.findAll();

		return deals.stream().filter(deal -> {
			Coordinates coordinates = deal.getOffer().getSubEntity().getCoordinates();
			return calculateDistance(userLat, userLon, coordinates.latitude(), coordinates.longitude()) <= radius;
		}).map(deal -> {
			Map<String, Object> dealMap = new HashMap<>();
			SubEntity subEntity = deal.getOffer().getSubEntity();
			dealMap.put("price", deal.getPrice().amount());
			dealMap.put("dealName", deal.getTitle());
			dealMap.put("subEntityName", subEntity.getName());
			dealMap.put("subEntityLogo", subEntity.getAvatarPath());
			dealMap.put("unit", deal.getDealUnityType());
			dealMap.put("date", deal.getCreatedAt());
			// dealMap.put("delivery", deal.get); a verifier
			return dealMap;
		}).collect(Collectors.toList());
	}

	// Filtrer les boxes en fonction de la distance
	private List<Map<String, Object>> getBoxes(double userLat, double userLon, double radius) {
		List<Box> boxes = boxRepository.findAll();

		return boxes.stream().filter(box -> {
			Coordinates coordinates = box.getOffer().getSubEntity().getCoordinates();
			return calculateDistance(userLat, userLon, coordinates.latitude(), coordinates.longitude()) <= radius;
		}).map(box -> {
			Map<String, Object> boxMap = new HashMap<>();
			SubEntity subEntity = box.getOffer().getSubEntity();
			boxMap.put("name", box.getTitle());
			boxMap.put("price", box.getOffer().getPrice().amount());
			boxMap.put("organizationLogo", subEntity.getAvatarPath());
			boxMap.put("quantity", box.getQuantity());
			boxMap.put("date", box.getCreatedAt());
			return boxMap;
		}).collect(Collectors.toList());
	}

	private List<Map<String, Object>> getPartners(double userLat, double userLon, double radius,
			List<SubEntity> subEntities) {
		// Filtrer les subEntities par distance
		return subEntities.stream().filter(subEntity -> {
			Coordinates coordinates = subEntity.getCoordinates();
			// Vérifier si la distance est inférieure ou égale au rayon
			return calculateDistance(userLat, userLon, coordinates.latitude(), coordinates.longitude()) <= radius;
		}).map(subEntity -> {
			// Créer un map pour chaque subEntity contenant les informations de
			// l'organisation et des offres
			Map<String, Object> subEntityMap = new HashMap<>();

			// Ajouter les informations de l'organisation associée au SubEntity
			OrganizationEntity org = subEntity.getOrganizationEntity();
			subEntityMap.put("subEntityName", subEntity.getName());
			subEntityMap.put("subEntityLogo", subEntity.getAvatarPath());
			subEntityMap.put("organizationName", org.getName());
			subEntityMap.put("organizationLogo", org.getAvatarPath());

			return subEntityMap;
		}).collect(Collectors.toList());
	}

	// Filtrer les hôtels et leurs offres
	private List<Map<String, Object>> getHotels(double userLat, double userLon, double radius, List<SubEntity> hotels) {
		return hotels.stream().filter(hotel -> {
			Coordinates coordinates = hotel.getCoordinates();
			return calculateDistance(userLat, userLon, coordinates.latitude(), coordinates.longitude()) <= radius;
		}).map(hotel -> {
			Map<String, Object> hotelMap = new HashMap<>();
			OrganizationEntity org = hotel.getOrganizationEntity();
			hotelMap.put("logo", org.getAvatarPath());
			hotelMap.put("cover", org.getCoverPath());
			hotelMap.put("hotelName", org.getName());

			List<Map<String, Object>> hotelOffers = new ArrayList<>();
			hotel.getOffers().forEach(offer -> {
				Map<String, Object> offerMap = new HashMap<>();
				offerMap.put("price", offer.getPrice().amount());
				offerMap.put("date", offer.getCreatedAt());
				hotelOffers.add(offerMap);
			});
			hotelMap.put("numberOfOffers", hotelOffers.size());
			hotelMap.put("offers", hotelOffers);
			return hotelMap;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Offer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Offer> findAll(Integer pageNumber, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Offer findById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Offer create(OfferRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Offer update(UUID id, OfferRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub

	}
}
