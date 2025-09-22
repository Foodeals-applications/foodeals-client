package net.foodeals.organizationEntity.application.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.Utils.DistanceCalculator;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.location.domain.repositories.AddressRepository;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.CountryRepository;
import net.foodeals.location.domain.repositories.RegionRepository;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.offer.application.dtos.responses.SupplementDealResponse;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.offer.domain.repositories.OfferRepository;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.organizationEntity.application.dtos.requests.SubEntityRequest;
import net.foodeals.organizationEntity.application.dtos.responses.*;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityStatus;
import net.foodeals.organizationEntity.domain.exceptions.SubEntityNotFoundException;
import net.foodeals.organizationEntity.domain.repositories.ActivityRepository;
import net.foodeals.organizationEntity.domain.repositories.OrganizationEntityRepository;
import net.foodeals.organizationEntity.domain.repositories.SolutionRepository;
import net.foodeals.organizationEntity.domain.repositories.SubEntityRepository;
import net.foodeals.product.application.dtos.responses.CategoryProductsResponse;
import net.foodeals.product.application.dtos.responses.PriceResponse;
import net.foodeals.product.application.dtos.responses.ProductOfferResponse;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.domain.exceptions.ProductNotFoundException;
import net.foodeals.product.domain.repositories.ProductCategoryRepository;
import net.foodeals.product.domain.repositories.ProductRepository;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.LikeRepository;
import net.foodeals.user.domain.repositories.UserRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SubEntityServiceImpl implements SubEntityService {


    private final SubEntityRepository repository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;
    private final AddressRepository addressRepository;
    private final ActivityRepository activityRepository;
    private final SolutionRepository solutionRepository;
    private final OrganizationEntityRepository organizationEntityRepository;
    private final LikeRepository likeRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final SubEntityRepository subEntityRepository;
    private final OfferRepository offerRepository;
    ;
    private final DealRepository dealRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper mapper;

    private static final double EARTH_RADIUS = 6371.0;

    @Value("${upload.directory}")
    private String uploadDir;

    @Override
    public List<SubEntity> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User user = userRepository.findByEmail(email).get();
            return user.getOrganizationEntity().getSubEntities();
        }
        return null;
    }

    @Override
    public Page<SubEntity> findAll(Integer pageNumber, Integer pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public SubEntity findById(UUID id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public SubEntity create(SubEntityRequest request) {
        SubEntity subEntity = new SubEntity();
        subEntity.setName(request.name());
        subEntity.setAvatarPath(request.avatarPath());
        subEntity.setCoverPath(request.coverPath());
        subEntity.setIFrame(request.iFrame());
        subEntity.setPhone(request.phone());
        subEntity.setEmail(request.email());
        List<Solution> managedSolutions = fetchSolutionsByNames(request.solutionNames());
        subEntity.setSolutions(managedSolutions);

        List<Activity> activities = fetchActivitiesByNames(request.activiteNames());
        subEntity.setActivities(activities);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).get();

        OrganizationEntity organizationEntity = user.getOrganizationEntity();

        subEntity.setOrganizationEntity(organizationEntity);

        User manager = userRepository.findById(request.managerId()).orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + request.managerId()));
        subEntity.setManager(manager);

        Address address = new Address();
        address.setCountry(countryRepository.findById(request.countryId()).orElseThrow(() -> new EntityNotFoundException("Country not found with id: " + request.countryId())));
        address.setCity(cityRepository.findById(request.cityId()).orElseThrow(() -> new EntityNotFoundException("City not found with id: " + request.cityId())));
        address.setRegion(regionRepository.findById(request.regionId()).orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + request.regionId())));
        address.setExtraAddress(request.exactAdresse());

        address = addressRepository.save(address);
        subEntity.setAddress(address);
        subEntity.setSubEntityStatus(SubEntityStatus.INACTIVE);
        return repository.save(subEntity);
    }

    @Override
    public SubEntity update(UUID id, SubEntityRequest dto) {

        SubEntity subEntity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SubEntity not found with id: " + id));

        subEntity.setName(dto.name());
        subEntity.setAvatarPath(dto.avatarPath());
        subEntity.setCoverPath(dto.coverPath());
        subEntity.setEmail(dto.email());
        subEntity.setPhone(dto.phone());
        subEntity.setIFrame(dto.iFrame());

        subEntity.setManager(userRepository.findById(dto.managerId()).orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + dto.managerId())));

        subEntity.setActivities(fetchActivitiesByNames(dto.activiteNames()));

        subEntity.setSolutions(fetchSolutionsByNames(dto.solutionNames()));

        Address address = addressRepository.findById(subEntity.getAddress().getId()).get();

        address.setCountry(countryRepository.findById(dto.countryId()).orElseThrow(() -> new EntityNotFoundException("Country not found with id: " + dto.countryId())));
        address.setCity(cityRepository.findById(dto.cityId()).orElseThrow(() -> new EntityNotFoundException("City not found with id: " + dto.cityId())));
        address.setRegion(regionRepository.findById(dto.regionId()).orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + dto.regionId())));

        subEntity.setAddress(address);

        return repository.save(subEntity);
    }

    @Override
    public SubEntity updateSubEntity(SubEntityRequest dto) {
        SubEntity subEntity = mapper.map(dto, SubEntity.class);
        return repository.save(subEntity);
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) throw new SubEntityNotFoundException(id);

        repository.softDelete(id);

    }

    private List<Solution> fetchSolutionsByNames(List<String> solutionNames) {
        if (solutionNames == null || solutionNames.isEmpty()) {
            throw new IllegalArgumentException("Solution names list cannot be null or empty.");
        }

        return solutionNames.stream().map(this::findSolutionByName).collect(Collectors.toList());
    }


    private Solution findSolutionByName(String solutionName) {
        return solutionRepository.findByName(solutionName);
    }


    private List<Activity> fetchActivitiesByNames(List<String> activityNames) {
        return activityNames.stream().map(activityName -> {
            Activity activity = activityRepository.findByName(activityName);
            if (activity == null) {
                throw new EntityNotFoundException("Activity not found with name: " + activityName);
            }
            return activity;
        }).collect(Collectors.toList());
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

    @Override
    public void deleteSubEntity(UUID id, String reason, String motif) {

        SubEntity subEntity = findById(id);
        if (!Objects.isNull(subEntity)) {
            subEntity.setReason(reason);
            subEntity.setMotif(motif);
            subEntity.setSubEntityStatus(SubEntityStatus.ARCHIVED);
            repository.save(subEntity);
            repository.softDelete(id);
        } else {
            throw new ProductNotFoundException(id);
        }

    }

    public Page<SubEntity> filterSubEntities(Instant startDate, Instant endDate, String raisonSociale, UUID managerId, String email, String phone, UUID cityId, UUID solutionId, Pageable pageable) {
        return repository.filterSubEntities(raisonSociale, managerId, email, phone, cityId, solutionId, startDate, endDate, pageable);
    }

    @Override
    public Page<SubEntity> getAllByStatus(String status, Pageable pageable) {
        SubEntityStatus subEntityStatus = SubEntityStatus.valueOf(status);
        return repository.findAllBySubEntityStatus(subEntityStatus, pageable);
    }

    @Override
    public SubEntity confirmSubEntity(UUID id) {
        SubEntity subEntity = findById(id);
        subEntity.setSubEntityStatus(SubEntityStatus.ACTIVE);
        ;
        return repository.save(subEntity);
    }


    @Override
    public List<Map<String, Object>> getStoreCountByDomains() {
        List<Object[]> results = repository.countStoresByDomains();
        List<Map<String, Object>> response = new ArrayList();

        for (Object[] result : results) {
            Map<String, Object> activityMap = new HashMap();
            activityMap.put("domain", result[0]);  // Nom de domaine
            activityMap.put("storeCount", result[1]); // Nombre de magasins
            response.add(activityMap);
        }

        return response;
    }


    @Override
    public SubEntityDetailsResponse getSubEntityDetails(UUID subEntityId, Integer userId) {
        // 1. Récupérer la sous-entité
        SubEntity subEntity = repository.findById(subEntityId).orElseThrow(() -> new EntityNotFoundException("SubEntity not found with id: " + subEntityId));

        // Détails de base
        String name = subEntity.getName();
        String logo = subEntity.getAvatarPath();
        String cover = subEntity.getCoverPath();
        Integer totalSales = repository.getTotalSalesBySubEntity(subEntityId);
        String address = subEntity.getAddress() != null ? subEntity.getAddress().getAddress() + " " + subEntity.getAddress().getCity().getName() : "N/A";
        String type = subEntity.getType().toString();

        // Détails avancés
        boolean isFavorite = likeRepository.existsBySubEntityIdAndUserId(subEntityId, userId);
        boolean deliveryFree = true;
        List<String> categoriesWithOffers = categoryRepository.findActiveCategoryNamesBySubEntity(subEntityId);

        // Produits en promotion
        List<ProductOfferResponse> productsOnOffer = productRepository.findProductsWithActiveOffers(subEntityId);


        // Produits triés par catégorie
        // Produits triés par catégorie
        List<CategoryProductsResponse> categorizedProducts = categoryRepository.findCategoriesBySubEntity(subEntityId).stream().map(category -> new CategoryProductsResponse(category.getName(), productRepository.findByCategoryAndSubEntity(category.getId(), subEntityId).stream().map(product -> {
            // Récupérer les prix et autres informations à partir des boxes et deals
            Double oldPrice = null;
            Double newPrice = null;
            // Deal : Si les offres de Box ne sont pas disponibles, vérifier les Deals
            Optional<Deal> optionalDeal = dealRepository.findActiveDealByProduct(product.getId());
            if (optionalDeal.isPresent()) {
                Deal deal = optionalDeal.get();
                oldPrice = deal.getOffer().getPrice().amount().doubleValue();
                newPrice = deal.getOffer().getSalePrice().amount().doubleValue();
            }

            return new ProductResponse(product.getId(), product.getProductImagePath(), product.getName(), product.getDescription(), new PriceResponse(oldPrice, newPrice), categoriesWithOffers, 0, subEntityId);
        }).collect(Collectors.toList()))).collect(Collectors.toList());

        return new SubEntityDetailsResponse(name, logo, cover, totalSales, address, type, categoriesWithOffers, productsOnOffer, categorizedProducts, subEntity.getNumberOfLikes(), deliveryFree, isFavorite, subEntity.getCoordinates());
    }

    @Override
    public HotelDetailsResponse getHotelDetails(UUID subEntityId) {
        SubEntity subEntity = repository.findById(subEntityId).orElse(null);
        if (subEntity != null) {
            Integer totalSales = repository.getTotalSalesBySubEntity(subEntityId);
            User connectedUser = userService.getConnectedUser();

            boolean isFavorite = likeRepository.existsBySubEntityIdAndUserId(subEntityId, connectedUser.getId());
            List<String> categoriesWithOffers = categoryRepository.findActiveCategoryNamesBySubEntity(subEntityId);

            // Produits en promotion
            List<ProductOfferResponse> productsOnOffer = productRepository.findProductsWithActiveOffers(subEntityId);


            // Produits triés par catégorie
            // Produits triés par catégorie
            List<CategoryProductsResponse> categorizedProducts = categoryRepository.findCategoriesBySubEntity(subEntityId).stream().map(category -> new CategoryProductsResponse(category.getName(), productRepository.findByCategoryAndSubEntity(category.getId(), subEntityId).stream().map(product -> {
                // Récupérer les prix et autres informations à partir des boxes et deals
                Double oldPrice = null;
                Double newPrice = null;
                // Deal : Si les offres de Box ne sont pas disponibles, vérifier les Deals
                Optional<Deal> optionalDeal = dealRepository.findActiveDealByProduct(product.getId());
                if (optionalDeal.isPresent()) {
                    Deal deal = optionalDeal.get();
                    oldPrice = deal.getOffer().getPrice().amount().doubleValue();
                    newPrice = deal.getOffer().getSalePrice().amount().doubleValue();
                }

                return new ProductResponse(product.getId(), product.getProductImagePath(), product.getName(), product.getDescription(), new PriceResponse(oldPrice, newPrice), categoriesWithOffers, 0, subEntityId);
            }).collect(Collectors.toList()))).collect(Collectors.toList());
            double distance = DistanceCalculator.calculateDistance(connectedUser.getCoordinates().latitude().doubleValue(), connectedUser.getCoordinates().longitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue());
            List<Deal> deals = new ArrayList<>();

            List<Offer> offers = offerRepository.getOffersBySubEntity(subEntity);
            for (Offer offer : offers) {
                deals.add(dealRepository.getDealByOfferId(offer.getId()));
            }

            List<DealResponse> dealsResponse = deals.stream()
                    .map(deal -> new DealResponse(
                            deal.getId(),
                            deal.getProduct().getName(),
                            deal.getProduct().getDescription(),
                            deal.getProduct().getProductImagePath(),
                            Date.from(deal.getCreatedAt()),
                            null,
                            null,
                            deal.getDealStatus(),
                            null
                    ))
                    .collect(Collectors.toList());

            return new HotelDetailsResponse
                    (subEntityId,
                            subEntity.getName(),
                            subEntity.getAvatarPath(),
                            subEntity.getCoverPath(),
                            totalSales,
                            subEntity.getAddress().getAddress() + "" + subEntity.getAddress().getCity().getName(),
                            subEntity.getModalityTypes(),
                            distance,
                            subEntity.getNumberOfLikes(),
                            subEntity.isFeeDelivered(),
                            subEntity.getNumberOfStars(),
                            dealsResponse,
                            categoriesWithOffers,
                            productsOnOffer,
                            categorizedProducts,
                            isFavorite,
                            subEntity.getCoordinates());
        }
        else return null;
    }

    @Override
    public RestaurantDetailsResponse getRestaurantDetails(UUID subEntityId) {
        SubEntity subEntity = repository.findById(subEntityId).orElse(null);
        if (subEntity != null) {

            Integer totalSales = repository.getTotalSalesBySubEntity(subEntityId);
            User connectedUser = userService.getConnectedUser();

            boolean isFavorite = likeRepository.existsBySubEntityIdAndUserId(subEntityId, connectedUser.getId());
            List<String> categoriesWithOffers = categoryRepository.findActiveCategoryNamesBySubEntity(subEntityId);

            // Produits en promotion
            List<ProductOfferResponse> productsOnOffer = productRepository.findProductsWithActiveOffers(subEntityId);


            // Produits triés par catégorie
            // Produits triés par catégorie
            List<CategoryProductsResponse> categorizedProducts = categoryRepository.findCategoriesBySubEntity(subEntityId).stream().map(category -> new CategoryProductsResponse(category.getName(), productRepository.findByCategoryAndSubEntity(category.getId(), subEntityId).stream().map(product -> {
                // Récupérer les prix et autres informations à partir des boxes et deals
                Double oldPrice = null;
                Double newPrice = null;
                // Deal : Si les offres de Box ne sont pas disponibles, vérifier les Deals
                Optional<Deal> optionalDeal = dealRepository.findActiveDealByProduct(product.getId());
                if (optionalDeal.isPresent()) {
                    Deal deal = optionalDeal.get();
                    oldPrice = deal.getOffer().getPrice().amount().doubleValue();
                    newPrice = deal.getOffer().getSalePrice().amount().doubleValue();
                }

                return new ProductResponse(product.getId(), product.getProductImagePath(), product.getName(), product.getDescription(), new PriceResponse(oldPrice, newPrice), categoriesWithOffers, 0, subEntityId);
            }).collect(Collectors.toList()))).collect(Collectors.toList());


            double distance = DistanceCalculator.calculateDistance(connectedUser.getCoordinates().latitude().doubleValue(), connectedUser.getCoordinates().longitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue());
            List<Deal> deals = new ArrayList<>();
            List<Offer> offers = offerRepository.getOffersBySubEntity(subEntity);
            for (Offer offer : offers) {
                deals.add(dealRepository.getDealByOfferId(offer.getId()));
            }

            List<DealResponse> dealsResponse = deals.stream().map(deal -> {
                // Récupérer les suppléments liés à ce deal
                List<SupplementDealResponse> supplementResponses = deal.getSupplements().stream()
                        .map(supplement -> new SupplementDealResponse(
                                supplement.getId(),
                                supplement.getName(),
                                supplement.getPrice(),
                                supplement.getSupplementImagePath(),
                                supplement.getSupplementCategory()
                        ))
                        .collect(Collectors.toList());

                return new DealResponse(
                        deal.getId(),
                        deal.getProduct().getName(),
                        deal.getProduct().getDescription(),
                        deal.getProduct().getProductImagePath(),
                        Date.from(deal.getCreatedAt()),
                        null,
                        null,
                        deal.getDealStatus(),
                        supplementResponses
                );
            }).collect(Collectors.toList());

            return new RestaurantDetailsResponse(subEntityId,
                    subEntity.getName(),
                    subEntity.getAvatarPath(),
                    subEntity.getCoverPath(),
                    totalSales,
                    subEntity.getAddress().getAddress() + "" + subEntity.getAddress().getCity().getName(),
                    subEntity.getModalityTypes(),
                    distance,
                    subEntity.getNumberOfLikes(),
                    subEntity.isFeeDelivered(),
                    subEntity.getNumberOfStars(),
                    dealsResponse,
                    categoriesWithOffers,
                    productsOnOffer,
                    categorizedProducts,
                    isFavorite,
                    subEntity.getCoordinates());
        } else return null;
    }

    @Override
    public BakeryDetailsResponse getBakeryDetails(UUID subEntityId) {
        SubEntity subEntity = repository.findById(subEntityId).orElse(null);
        if (subEntity != null) {
            Integer totalSales = repository.getTotalSalesBySubEntity(subEntityId);
            User connectedUser = userService.getConnectedUser();

            boolean isFavorite = likeRepository.existsBySubEntityIdAndUserId(subEntityId, connectedUser.getId());
            List<String> categoriesWithOffers = categoryRepository.findActiveCategoryNamesBySubEntity(subEntityId);

            // Produits en promotion
            List<ProductOfferResponse> productsOnOffer = productRepository.findProductsWithActiveOffers(subEntityId);


            // Produits triés par catégorie
            // Produits triés par catégorie
            List<CategoryProductsResponse> categorizedProducts = categoryRepository.findCategoriesBySubEntity(subEntityId).stream().map(category -> new CategoryProductsResponse(category.getName(), productRepository.findByCategoryAndSubEntity(category.getId(), subEntityId).stream().map(product -> {
                // Récupérer les prix et autres informations à partir des boxes et deals
                Double oldPrice = null;
                Double newPrice = null;
                // Deal : Si les offres de Box ne sont pas disponibles, vérifier les Deals
                Optional<Deal> optionalDeal = dealRepository.findActiveDealByProduct(product.getId());
                if (optionalDeal.isPresent()) {
                    Deal deal = optionalDeal.get();
                    oldPrice = deal.getOffer().getPrice().amount().doubleValue();
                    newPrice = deal.getOffer().getSalePrice().amount().doubleValue();
                }

                return new ProductResponse(product.getId(), product.getProductImagePath(), product.getName(), product.getDescription(), new PriceResponse(oldPrice, newPrice), categoriesWithOffers, 0, subEntityId);
            }).collect(Collectors.toList()))).collect(Collectors.toList());

            double distance = DistanceCalculator.calculateDistance(connectedUser.getCoordinates().latitude().doubleValue(), connectedUser.getCoordinates().longitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue());
            List<Deal> deals = new ArrayList<>();
            List<Offer> offers = offerRepository.getOffersBySubEntity(subEntity);
            for (Offer offer : offers) {
                deals.add(dealRepository.getDealByOfferId(offer.getId()));
            }

            List<DealResponse> dealsResponse = deals.stream().map(deal -> {
                // Récupérer les suppléments liés à ce deal
                List<SupplementDealResponse> supplementResponses = deal.getSupplements().stream()
                        .map(supplement -> new SupplementDealResponse(
                                supplement.getId(),
                                supplement.getName(),
                                supplement.getPrice(),
                                supplement.getSupplementImagePath(),
                                supplement.getSupplementCategory()
                        ))
                        .collect(Collectors.toList());

                return new DealResponse(
                        deal.getId(),
                        deal.getProduct().getName(),
                        deal.getProduct().getDescription(),
                        deal.getProduct().getProductImagePath(),
                        Date.from(deal.getCreatedAt()),
                        null,
                        null,
                        deal.getDealStatus(),
                        supplementResponses
                );
            }).collect(Collectors.toList());

            return new BakeryDetailsResponse
                    (subEntityId,
                            subEntity.getName(),
                            subEntity.getAvatarPath(),
                            subEntity.getCoverPath(),
                            totalSales,
                            subEntity.getAddress().getAddress() + "" + subEntity.getAddress().getCity().getName(),
                            subEntity.getModalityTypes(),
                            distance,
                            subEntity.getNumberOfLikes(),
                            subEntity.isFeeDelivered(),
                            subEntity.getNumberOfStars(),
                            dealsResponse,
                            categoriesWithOffers,
                            productsOnOffer,
                            categorizedProducts,
                            isFavorite,
                            subEntity.getCoordinates());
        } else return null;
    }

    @Override
    public IndustryDetailsResponse getIndustryDetails(UUID subEntityId) {
        SubEntity subEntity = repository.findById(subEntityId).orElse(null);
        if (subEntity != null) {
            Integer totalSales = repository.getTotalSalesBySubEntity(subEntityId);
            User connectedUser = userService.getConnectedUser();

            boolean isFavorite = likeRepository.existsBySubEntityIdAndUserId(subEntityId, connectedUser.getId());
            List<String> categoriesWithOffers = categoryRepository.findActiveCategoryNamesBySubEntity(subEntityId);

            // Produits en promotion
            List<ProductOfferResponse> productsOnOffer = productRepository.findProductsWithActiveOffers(subEntityId);


            // Produits triés par catégorie
            // Produits triés par catégorie
            List<CategoryProductsResponse> categorizedProducts = categoryRepository.findCategoriesBySubEntity(subEntityId).stream().map(category -> new CategoryProductsResponse(category.getName(), productRepository.findByCategoryAndSubEntity(category.getId(), subEntityId).stream().map(product -> {
                // Récupérer les prix et autres informations à partir des boxes et deals
                Double oldPrice = null;
                Double newPrice = null;
                // Deal : Si les offres de Box ne sont pas disponibles, vérifier les Deals
                Optional<Deal> optionalDeal = dealRepository.findActiveDealByProduct(product.getId());
                if (optionalDeal.isPresent()) {
                    Deal deal = optionalDeal.get();
                    oldPrice = deal.getOffer().getPrice().amount().doubleValue();
                    newPrice = deal.getOffer().getSalePrice().amount().doubleValue();
                }

                return new ProductResponse(product.getId(), product.getProductImagePath(), product.getName(), product.getDescription(), new PriceResponse(oldPrice, newPrice), categoriesWithOffers, 0, subEntityId);
            }).collect(Collectors.toList()))).collect(Collectors.toList());
            double distance = DistanceCalculator.calculateDistance(connectedUser.getCoordinates().latitude().doubleValue(), connectedUser.getCoordinates().longitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue());
            return new IndustryDetailsResponse
                    (subEntityId,
                            subEntity.getName(),
                            subEntity.getAvatarPath(),
                            subEntity.getCoverPath(),
                            totalSales,
                            subEntity.getAddress().getAddress() + "" + subEntity.getAddress().getCity().getName(),
                            subEntity.getModalityTypes(),
                            distance,
                            subEntity.getNumberOfLikes(),
                            subEntity.isFeeDelivered(),
                            subEntity.getNumberOfStars(),
                            null,
                            categoriesWithOffers,
                            productsOnOffer,
                            categorizedProducts,
                            isFavorite,
                            subEntity.getCoordinates());

        } else return null;
    }

    @Override
    public AgriculturDetailsResponse getAgricultureDetails(UUID subEntityId) {
        SubEntity subEntity = repository.findById(subEntityId).orElse(null);
        if (subEntity != null) {
            Integer totalSales = repository.getTotalSalesBySubEntity(subEntityId);
            User connectedUser = userService.getConnectedUser();

            boolean isFavorite = likeRepository.existsBySubEntityIdAndUserId(subEntityId, connectedUser.getId());
            List<String> categoriesWithOffers = categoryRepository.findActiveCategoryNamesBySubEntity(subEntityId);

            // Produits en promotion
            List<ProductOfferResponse> productsOnOffer = productRepository.findProductsWithActiveOffers(subEntityId);


            // Produits triés par catégorie
            // Produits triés par catégorie
            List<CategoryProductsResponse> categorizedProducts = categoryRepository.findCategoriesBySubEntity(subEntityId).stream().map(category -> new CategoryProductsResponse(category.getName(), productRepository.findByCategoryAndSubEntity(category.getId(), subEntityId).stream().map(product -> {
                // Récupérer les prix et autres informations à partir des boxes et deals
                Double oldPrice = null;
                Double newPrice = null;
                // Deal : Si les offres de Box ne sont pas disponibles, vérifier les Deals
                Optional<Deal> optionalDeal = dealRepository.findActiveDealByProduct(product.getId());
                if (optionalDeal.isPresent()) {
                    Deal deal = optionalDeal.get();
                    oldPrice = deal.getOffer().getPrice().amount().doubleValue();
                    newPrice = deal.getOffer().getSalePrice().amount().doubleValue();
                }

                return new ProductResponse(product.getId(), product.getProductImagePath(), product.getName(), product.getDescription(), new PriceResponse(oldPrice, newPrice), categoriesWithOffers, 0, subEntityId);
            }).collect(Collectors.toList()))).collect(Collectors.toList());
            double distance = DistanceCalculator.calculateDistance(connectedUser.getCoordinates().latitude().doubleValue(), connectedUser.getCoordinates().longitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue(), subEntity.getCoordinates().latitude().doubleValue());
            return new AgriculturDetailsResponse
                    (subEntityId,
                            subEntity.getName(),
                            subEntity.getAvatarPath(),
                            subEntity.getCoverPath(),
                            totalSales,
                            subEntity.getAddress().getAddress() + "" + subEntity.getAddress().getCity().getName(),
                            subEntity.getModalityTypes(),
                            distance,
                            subEntity.getNumberOfLikes(),
                            subEntity.isFeeDelivered(),
                            subEntity.getNumberOfStars(),
                            null,
                            categoriesWithOffers,
                            productsOnOffer,
                            categorizedProducts,
                            isFavorite,
                            subEntity.getCoordinates());
        } else return null;
    }

    @Override
    public List<RestaurantResponse> getListOfRestaurants(User user) {

        List<SubEntity> restaurants = subEntityRepository.findByDomaineName("Restaurants");

        return restaurants.stream()

                .map(subEntity -> {
                    double distance = DistanceCalculator.calculateDistance(
                            user.getCoordinates().latitude().doubleValue(),
                            user.getCoordinates().longitude().doubleValue(),
                            subEntity.getCoordinates().latitude().doubleValue(),
                            subEntity.getCoordinates().longitude().doubleValue() // <-- ici était l'erreur
                    );
                    return new RestaurantResponse(
                            subEntity.getId(),
                            subEntity.getAvatarPath(),
                            subEntity.getCoverPath(),
                            subEntity.getName(),
                            distance,
                            subEntity.getNumberOfLikes(),
                            subEntity.getNumberOfStars()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<HotelResponse> getListOfHotels(User user) {
        List<SubEntity> hotels = subEntityRepository.findByDomaineName("Hôtels");

        return hotels.stream()

                .map(subEntity -> {
                    double distance = DistanceCalculator.calculateDistance(
                            user.getCoordinates().latitude().doubleValue(),
                            user.getCoordinates().longitude().doubleValue(),
                            subEntity.getCoordinates().latitude().doubleValue(),
                            subEntity.getCoordinates().longitude().doubleValue() // <-- ici était l'erreur
                    );
                    return new HotelResponse(
                            subEntity.getId(),
                            subEntity.getAvatarPath(),
                            subEntity.getCoverPath(),
                            subEntity.getName(),
                            distance,
                            subEntity.getNumberOfLikes(),
                            subEntity.getNumberOfStars()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BakeryResponse> getListOfBakeries(User user) {
        List<SubEntity> bakeries = subEntityRepository.findByDomaineName("Pâtisseries");

        return bakeries.stream()

                .map(subEntity -> {
                    double distance = DistanceCalculator.calculateDistance(
                            user.getCoordinates().latitude().doubleValue(),
                            user.getCoordinates().longitude().doubleValue(),
                            subEntity.getCoordinates().latitude().doubleValue(),
                            subEntity.getCoordinates().longitude().doubleValue() // <-- ici était l'erreur
                    );
                    return new BakeryResponse(
                            subEntity.getId(),
                            subEntity.getAvatarPath(),
                            subEntity.getCoverPath(),
                            subEntity.getName(),
                            distance,
                            subEntity.getNumberOfLikes(),
                            subEntity.getNumberOfStars()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AgricultureResponse> getListOfAgrucultures(User user) {
        List<SubEntity> agricultures = subEntityRepository.findByDomaineName("Agricultures");

        return agricultures.stream()

                .map(subEntity -> {
                    double distance = DistanceCalculator.calculateDistance(
                            user.getCoordinates().latitude().doubleValue(),
                            user.getCoordinates().longitude().doubleValue(),
                            subEntity.getCoordinates().latitude().doubleValue(),
                            subEntity.getCoordinates().longitude().doubleValue() // <-- ici était l'erreur
                    );
                    return new AgricultureResponse(
                            subEntity.getId(),
                            subEntity.getAvatarPath(),
                            subEntity.getCoverPath(),
                            subEntity.getName(),
                            distance,
                            subEntity.getNumberOfLikes(),
                            subEntity.getNumberOfStars()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SpotlightStore> getSpotlightStores() {
        User user = userService.getConnectedUser();

        List<SubEntity> featuredStores = subEntityRepository.findByIsFeaturedTrue();

        return featuredStores.stream().map(subEntity -> {
            double distance = DistanceCalculator.calculateDistance(
                    user.getCoordinates().latitude(),
                    user.getCoordinates().longitude(),
                    subEntity.getCoordinates().latitude(),
                    subEntity.getCoordinates().longitude()
            );
            return new SpotlightStore(
                    subEntity.getId(),
                    subEntity.getName(),
                    subEntity.getAvatarPath(),
                    subEntity.getCoverPath(),
                    distance,
                    subEntity.getNumberOfLikes(),
                    subEntity.getNumberOfStars()
            );
        }).collect(Collectors.toList());
    }


    public List<BestSellerResponse> getBestSellers(Double salesThreshold) {

        Double globalTotalSales = orderRepository.findGlobalTotalSales();
        List<Object[]> bestSellersData = orderRepository.findBestSellers();

        return bestSellersData.stream().map(data -> {
            UUID subEntityId = (UUID) data[0];
            BigDecimal totalSales = (BigDecimal) data[1];

            SubEntity subEntity = subEntityRepository.findById(subEntityId).orElseThrow(() -> new IllegalArgumentException("SubEntity not found"));
            if (totalSales.compareTo(BigDecimal.valueOf(salesThreshold)) > 0) {
                return new BestSellerResponse(subEntity.getId(), subEntity.getName(), subEntity.getAvatarPath(), subEntity.getCoverPath(), totalSales.doubleValue(), totalSales.doubleValue() / globalTotalSales.doubleValue() * 100, calculateDeliveryFee(totalSales.doubleValue()));
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }


    public SubEntityResponse getStoreDetails(UUID subEntityId) {
        // Récupérer la sous-entité par son ID
        SubEntity subEntity = subEntityRepository.findById(subEntityId).orElseThrow(() -> new EntityNotFoundException("Magasin non trouvé"));

        // Construire manuellement l'objet de réponse
        return new SubEntityResponse(subEntity.getName(), subEntity.getAddress().getAddress() + " " + subEntity.getAddress().getCity().getName(), "0", 500.0, 0, 0);
    }


    private Double calculateDeliveryFee(Double totalSales) {

        if (totalSales > 5000) {
            return 0.0;
        }
        return 20.0;
    }


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


    public List<StoreMapDTO> getStoresOnMap() {
        return subEntityRepository.findAll()
                .stream()
                .filter(sub -> sub.getCoordinates() != null)
                .map(sub -> new StoreMapDTO(
                        sub.getId(),
                        sub.getName(),
                        sub.getAvatarPath(),
                        sub.getCoordinates().latitude(),
                        sub.getCoordinates().longitude()
                ))
                .collect(Collectors.toList());
    }
}
