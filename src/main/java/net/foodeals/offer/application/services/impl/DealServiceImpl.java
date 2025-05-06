package net.foodeals.offer.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.requests.DealDto;
import net.foodeals.offer.application.dtos.responses.DealDetailsResponse;
import net.foodeals.offer.application.dtos.responses.OpenTimeResponse;
import net.foodeals.offer.application.dtos.responses.SupplementDealResponse;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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


    private DealDetailsResponse mapToDealDetailsResponse(Deal deal) {
        DealDetailsResponse dealDetailsResponse = mapDealToDealDetailsResponse(deal);
        return dealDetailsResponse;
    }


    private DealDetailsResponse mapDealToDealDetailsResponse(Deal deal) {
        DealDetailsResponse response = new DealDetailsResponse();
        response.setId(deal.getId());
        response.setPhotoPath(deal.getProduct().getProductImagePath());
        response.setTitle(deal.getTitle());
        response.setDescription(deal.getDescription());
        response.setNumberOfFeedback(deal.getOffer().getNumberOfFeedBack());
        response.setNumberOfStars(deal.getOffer().getNumberOfStars());
        response.setEstimatedDeliveryTime(0f);
        List<OpenTime> openTimes = deal.getOffer().getOpenTime();
        List<OpenTimeResponse> openTimeResponses = new ArrayList<>();
        for (OpenTime openTime : openTimes) {
            openTimeResponses.add(new OpenTimeResponse(openTime.getId(), openTime.getDate(), openTime.getFrom(), openTime.getTo()));
        }
        response.setOpenTime(openTimeResponses);
        response.setModalityTypes(deal.getOffer().getModalityTypes());
        //List<Box> similarBoxes = new ArrayList<>();

        List<SimilarProductResponse> similarProducts = new ArrayList<>();
        response.setSimilarProductResponses(similarProducts);
        response.setCategoryName(null);
        List<SupplementDealResponse> supplementResponses = deal.getSupplements().stream()
                .map(supplement -> new SupplementDealResponse(
                        supplement.getId(),
                        supplement.getName(),
                        supplement.getPrice(),
                        supplement.getSupplementImagePath()
                )).collect(Collectors.toList());
        return response;

    }
}
