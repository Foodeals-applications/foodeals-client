package net.foodeals.offer.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.Utils.DistanceCalculator;
import net.foodeals.offer.application.dtos.requests.DealDto;
import net.foodeals.offer.application.dtos.responses.*;
import net.foodeals.offer.application.services.DealService;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.OpenTime;
import net.foodeals.offer.domain.exceptions.DealNotFoundException;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.offer.domain.repositories.OpenTimeRepository;
import net.foodeals.product.application.dtos.responses.SimilarProductResponse;
import net.foodeals.product.application.services.ProductCategoryService;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.product.application.services.ProductSubCategoryService;
import net.foodeals.product.domain.repositories.SupplementRepository;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
class DealServiceImpl implements DealService {

    private final DealRepository repository;
    private final OpenTimeRepository openTimeRepository;
    private final SupplementRepository supplementRepository;
    private final ProductService productService;
    private final ProductCategoryService categoryService;
    private final ProductSubCategoryService subCategoryService;
    private final UserService userService;

    @Value("${upload.directory}")
    private String uploadDir;

    @Override
    public Deal findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new DealNotFoundException(id));
    }

    @Override
    public Deal create(DealDto dto) {

        return null;

    }

    @Override
    public Deal update(UUID id, DealDto dto) {
        return null;
    }

    @Override
    public List<Deal> findAll() {
        return List.of();
    }

    @Override
    public Page<Deal> findAll(Integer pageNumber, Integer pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id))
            throw new DealNotFoundException(id);

        repository.softDelete(id);
    }

    @Override
    public DealDetailsResponse getDetailsDeal(UUID id) {
        Deal deal = repository.findById(id).orElseThrow(() -> new DealNotFoundException(id));
        return mapToDealDetailsResponse(deal);
    }

    @Override
    public FeaturedDealsResponse getFeaturedDeals() {
        // ⚡ Ici on filtre seulement les deals "featured" (selon un flag en DB)
        List<Deal> featuredDeals = repository.findByIsFeaturedTrueAndIsActiveTrue();

        List<DealFeaturedResponse> responses = featuredDeals.stream()
                .map(deal -> new DealFeaturedResponse(
                        deal.getId(),
                        deal.getTitle(),
                        deal.getDescription(),
                        deal.getOffer().getSalePrice().amount().doubleValue(),
                        deal.getOffer().getPrice().amount().doubleValue(),
                        deal.getOffer().getSubEntity().getName(),
                        deal.getOffer().getSubEntity().getAvatarPath()
                ))
                .toList();

        return new FeaturedDealsResponse(responses);
    }


    private DealDetailsResponse mapToDealDetailsResponse(Deal deal) {
        DealDetailsResponse dealDetailsResponse = mapDealToDealDetailsResponse(deal);
        return dealDetailsResponse;
    }


    private DealDetailsResponse mapDealToDealDetailsResponse(Deal deal) {
        User user = userService.getConnectedUser();
        DealDetailsResponse response = new DealDetailsResponse();
        response.setId(deal.getId());
        response.setPhotoPath(deal.getProduct().getProductImagePath());
        response.setTitle(deal.getTitle());
        response.setDescription(deal.getDescription());
        double distance = DistanceCalculator.calculateDistance(user.getCoordinates().latitude().doubleValue(),
        		user.getCoordinates().longitude().doubleValue(),
        	deal.getOffer().getSubEntity().getCoordinates().latitude().doubleValue(),
        	deal.getOffer().getSubEntity().getCoordinates().latitude().doubleValue());
        response.setNumberOfFeedback(deal.getOffer().getNumberOfFeedBack());
        response.setDistance(distance);
        response.setNumberOfStars(deal.getOffer().getNumberOfStars());
        response.setReviews((response.getNumberOfFeedback() / response.getNumberOfStars()));
        response.setEstimatedDeliveryTime(0f);
        List<OpenTime> openTimes = deal.getOffer().getOpenTime();
        List<OpenTimeResponse> openTimeResponses = new ArrayList<>();
        for (OpenTime openTime : openTimes) {
            openTimeResponses.add(new OpenTimeResponse(openTime.getId(), openTime.getDate(), openTime.getFrom(), openTime.getTo()));
        }
        response.setOpenTime(openTimeResponses);
        response.setModalityTypes(deal.getOffer().getModalityTypes());
        response.setCategoryName(null);
        response.setAddress(deal.getOffer().getSubEntity().getAddress().getAddress() + " "
                + deal.getOffer().getSubEntity().getAddress().getCity().getName());
        response.setFavorite(user.getFavorisOffers().contains(deal.getOffer()));

        BigDecimal oldPrice = deal.getOffer().getPrice().amount();
        BigDecimal newPrice = deal.getOffer().getSalePrice().amount();

        response.setNewPrice(newPrice);
        response.setOldPrice(oldPrice);

        if (oldPrice != null && oldPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = oldPrice.subtract(newPrice)
                    .divide(oldPrice, 2, RoundingMode.HALF_UP) // pour obtenir un ratio avec 2 décimales
                    .multiply(BigDecimal.valueOf(100));

            response.setDiscount(discount.intValue()); // convertit en Integer
        } else {
            response.setDiscount(0);
        }

        Map<String, List<SupplementDealResponse>> supplementResponses = deal.getSupplements().stream()
                .map(supplement -> new SupplementDealResponse(
                        supplement.getId(),
                        supplement.getName(),
                        supplement.getPrice(),
                        supplement.getSupplementImagePath(),
                        supplement.getSupplementCategory()
                ))
                .collect(Collectors.groupingBy(
                        res -> res.supplementCategory().name(), // clé = nom de l'enum en String
                        LinkedHashMap::new,                                // optionnel : garde l'ordre d'insertion
                        Collectors.toList()
                ));
        response.setSupplementResponses(supplementResponses);

        List<Deal>deals=repository.findSimilarDealsByProductCategory(deal.getId(),deal.getProduct().getCategory().getName());
        List<SimilarDealResponse>similarDealResponses=deals.stream().map(
                d->new SimilarDealResponse(d.getId(),
                        d.getTitle(),
                        d.getProduct().getProductImagePath(),
                        d.getOffer().getSalePrice().amount(),
                        d.getOffer().getPrice().amount())
        ).collect(Collectors.toList());
        response.setSimilarDeals(similarDealResponses);
        return response;

    }
}
