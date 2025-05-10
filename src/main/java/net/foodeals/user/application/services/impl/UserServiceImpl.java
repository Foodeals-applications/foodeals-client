package net.foodeals.user.application.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.foodeals.common.Utils.PriceUtils;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.offer.domain.repositories.OfferRepository;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.repositories.ProductRepository;
import net.foodeals.user.application.dtos.responses.FavorisOfferPartenerResponse;
import net.foodeals.user.application.dtos.responses.FavorisOfferResponse;
import net.foodeals.user.application.dtos.responses.InfosProfileResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.location.domain.entities.City;
import net.foodeals.location.domain.entities.Country;
import net.foodeals.location.domain.repositories.AddressRepository;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.CountryRepository;
import net.foodeals.user.application.dtos.requests.InfosProfileRequest;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.exceptions.UserNotFoundException;
import net.foodeals.user.domain.repositories.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

	private final UserRepository repository;
	private final DealRepository dealRepository;
	private final CountryRepository countryRepository;
	private final CityRepository cityRepository;
	private final AddressRepository addressRepository;

	@Value("${upload.directory}")
	private String uploadDir;
	
	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Page<User> findAll(Integer pageNumber, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public User findById(Integer id) {
		return repository.findById(id).orElseGet(null);
	}
	@Override
	public User create(UserRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public User update(Integer id, UserRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public User findByEmail(String email) {
		return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
	}
	
	@Override
	public User changeAvatarPhoto(Integer id, String avatarPath) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public User getConnectedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String email = userDetails.getUsername();
			return repository.findByEmail(email).get();
		}

		return null;

	}
	@Override
	public void changePassword(Integer idUser, String password) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public User setPositionClient(Integer id ,Coordinates coordinates, int raduis) {
		User user =repository.findById(id).orElse(null);
		if(!Objects.isNull(user)) {
			user.setCoordinates(coordinates);
			user.setRaduis(raduis);
			return repository.save(user);
		}
		return null;
	}

	@Override
	public List<FavorisOfferResponse> getListFavorisOffers() {
		User user = getConnectedUser();
		if(Objects.isNull(user.getFavorisOffers())) {
			return maptoFavorisOffers(user.getFavorisOffers());
		}
		return new ArrayList<>() ;
	}

	@Override
	public List<FavorisOfferPartenerResponse> getListFavorisOffersPartners() {
		User user = getConnectedUser();
		if(Objects.isNull(user.getFavorisOffers())) {
			return maptoFavorisOffersPartners(user.getFavorisOffers());
		}
		return new ArrayList<>() ;
	}


	public List<FavorisOfferResponse> maptoFavorisOffers(List<Offer> favorisOffers) {
		List<FavorisOfferResponse> favorisOfferResponses = new ArrayList<>();

		List<Deal> deals = favorisOffers.stream()
				.map(offer -> dealRepository.getDealByOfferId(offer.getId()))
				.collect(Collectors.toList());

		for (Deal deal : deals) {
			Product product = deal.getProduct();

			FavorisOfferResponse response = new FavorisOfferResponse();
			response.setDealId(deal.getId());
			response.setPhotoProduct(product.getProductImagePath());
			response.setNameProduct(product.getName());
			response.setOldPrice(deal.getOffer().getPrice());
			response.setNewPrice(deal.getOffer().getSalePrice());
			response.setPercentageReduction(PriceUtils.calculatePercentageReduction(deal.getOffer().getPrice().amount(),
					deal.getOffer().getSalePrice().amount()));
			response.setFeeDelivered(true); // to do

			favorisOfferResponses.add(response);
		}

		return favorisOfferResponses;
	}


	public List<FavorisOfferPartenerResponse> maptoFavorisOffersPartners(List<Offer> favorisOffers) {
		List<FavorisOfferPartenerResponse> favorisOfferPartenerResponse = new ArrayList<>();

		List<Deal> deals = favorisOffers.stream()
				.map(offer -> dealRepository.getDealByOfferId(offer.getId()))
				.collect(Collectors.toList());

		for (Deal deal : deals) {
			Product product = deal.getProduct();

			FavorisOfferPartenerResponse response = new FavorisOfferPartenerResponse();
			response.setDealId(deal.getId());
			response.setNamePartner(deal.getOffer().getSubEntity().getName());
			response.setPhotoPartner(deal.getOffer().getSubEntity().getAvatarPath());
			response.setPhotoProduct(product.getProductImagePath());
			response.setNameProduct(product.getName());
			response.setOldPrice(deal.getOffer().getPrice());
			response.setNewPrice(deal.getOffer().getSalePrice());
			response.setPercentageReduction(PriceUtils.calculatePercentageReduction(deal.getOffer().getPrice().amount(),
					deal.getOffer().getSalePrice().amount()));
			response.setFeeDelivered(true); // to do
			favorisOfferPartenerResponse.add(response);
		}

		return favorisOfferPartenerResponse;
	}
	@Override
	public InfosProfileResponse getInfosProfile() {
		User user =getConnectedUser();
		if(user!=null) {
		   InfosProfileResponse response=new InfosProfileResponse(user.getId(),
				   user.getAvatarPath(),
				   user.getName(),
				   user.getEmail(),
				   user.getPhone(), 
				   user.getAddress().getCountry().getName(), 
				   user.getAddress().getCity().getName(),
				   user.getAddress().getZip(),
				   user.getDateOfBirth())	;
		   return response;
		}
		return null;
	}
	@Override
	public InfosProfileResponse updateInfosProfile(InfosProfileRequest request) {
		
		User user =getConnectedUser();
		if(user!=null) {
		   user.setAvatarPath(request.getAvatarPath());
		   user.setName(request.getName());
		   user.setEmail(request.getEmailAddress());
		   user.setPhone(request.getPhone());
		   Country country=countryRepository.findByName(request.getCountryName());
		   Address address=user.getAddress();
		   if(country!=null) {
			   address.setCountry(country);
		   }
		   City city =cityRepository.findByName(request.getCityName());
		   if(city!=null) {
			   address.setCity(city);
		   }
		   address.setZip(request.getZip());
		   user.setDateOfBirth(request.getBirdhayDay());
		   user=repository.saveAndFlush(user);
		   InfosProfileResponse response=new InfosProfileResponse(user.getId(),
				   user.getAvatarPath(),
				   user.getName(),
				   user.getEmail(),
				   user.getPhone(), 
				   user.getAddress().getCountry().getName(), 
				   user.getAddress().getCity().getName(),
				   user.getAddress().getZip(),
				   user.getDateOfBirth())	;
		   return response;
		}
		return null;
	}


}