package net.foodeals.user.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.Utils.PriceUtils;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.location.domain.entities.City;
import net.foodeals.location.domain.entities.Country;
import net.foodeals.location.domain.repositories.AddressRepository;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.CountryRepository;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.user.application.dtos.requests.InfosProfileRequest;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.application.dtos.responses.FavorisOfferPartenerResponse;
import net.foodeals.user.application.dtos.responses.FavorisOfferResponse;
import net.foodeals.user.application.dtos.responses.InfosProfileResponse;
import net.foodeals.user.application.dtos.responses.UserStatisticsResponse;
import net.foodeals.user.application.services.RoleService;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.Role;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.exceptions.UserNotFoundException;
import net.foodeals.user.domain.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final DealRepository dealRepository;
    private final RoleService roleService;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
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
    @Transactional
    public User create(UserRequest request) {
        final User user = modelMapper.map(request, User.class);
        final Role role = roleService.findById(request.roleId());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.password()));
        return this.repository.save(user);
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
    public String uploadAvatar(Integer userId, MultipartFile file) throws IOException {
        User user = findById(userId);
        String avatarPath =saveFile(file);
        return avatarPath;
    }

    @Override
    public String saveFile(MultipartFile file) {

        Path path = Paths.get(uploadDir, file.getOriginalFilename());
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Problem while saving the file.", e);
        }

        return file.getOriginalFilename();
    }


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
    public User setPositionClient(Integer id, Coordinates coordinates, int raduis) {
        User user = repository.findById(id).orElse(null);
        if (!Objects.isNull(user)) {
            user.setCoordinates(coordinates);
            user.setRaduis(raduis);
            return repository.save(user);
        }
        return null;
    }

    @Override
    public List<FavorisOfferResponse> getListFavorisOffers() {
        User user = getConnectedUser();
        if (Objects.isNull(user.getFavorisOffers())) {
            return maptoFavorisOffers(user.getFavorisOffers());
        }
        return new ArrayList<>();
    }

    @Override
    public UserStatisticsResponse getStatistics() {
        User user = getConnectedUser();

        double savedAmount = 0.0;
        int productsSaved = 0;
        double co2Saved = 0.0;

        for (Order order : user.getOrders()) {
            Offer offer = order.getOffer();
            if (offer == null) continue;

            double originalPrice = offer.getPrice() != null ? offer.getPrice().amount().doubleValue() : 0.0;
            double salePrice = offer.getSalePrice() != null ? offer.getSalePrice().amount().doubleValue() : originalPrice;

            int quantity = order.getQuantity() != null ? order.getQuantity() : 1;

            savedAmount += (originalPrice - salePrice) * quantity;
            productsSaved += quantity;
        }

        // Exemple : chaque produit sauvé = 0.41 kg de CO2 évité
        co2Saved = productsSaved * 0.41;

        return new UserStatisticsResponse(savedAmount, productsSaved, co2Saved);
    }

    @Override
    public List<FavorisOfferPartenerResponse> getListFavorisOffersPartners() {
        User user = getConnectedUser();
        if (Objects.isNull(user.getFavorisOffers())) {
            return maptoFavorisOffersPartners(user.getFavorisOffers());
        }
        return new ArrayList<>();
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
        User user = getConnectedUser();
        if (user != null) {
            InfosProfileResponse response = new InfosProfileResponse(user.getId(),
                    user.getAvatarPath(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getAddress().getCountry().getName(),
                    user.getAddress().getCity().getName(),
                    user.getAddress().getZip(),
                    user.getDateOfBirth());
            return response;
        }
        return null;
    }

    @Override
    public InfosProfileResponse updateInfosProfile(InfosProfileRequest request) {

        User user = getConnectedUser();
        if (user != null) {
            user.setAvatarPath(request.getAvatarPath());
            user.setName(request.getName());
            user.setEmail(request.getEmailAddress());
            user.setPhone(request.getPhone());
            Country country = countryRepository.findByName(request.getCountryName());
            Address address = user.getAddress();
            if (country != null) {
                address.setCountry(country);
            }
            City city = cityRepository.findByName(request.getCityName());
            if (city != null) {
                address.setCity(city);
            }
            address.setZip(request.getZip());
            user.setDateOfBirth(request.getBirdhayDay());
            user = repository.saveAndFlush(user);
            InfosProfileResponse response = new InfosProfileResponse(user.getId(),
                    user.getAvatarPath(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getAddress().getCountry().getName(),
                    user.getAddress().getCity().getName(),
                    user.getAddress().getZip(),
                    user.getDateOfBirth());
            return response;
        }
        return null;
    }


}
