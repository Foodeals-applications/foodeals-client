package net.foodeals.offer.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.Utils.DistanceCalculator;
import net.foodeals.common.Utils.PriceUtils;
import net.foodeals.offer.application.dtos.requests.DealDto;
import net.foodeals.offer.application.dtos.responses.DealDetailsResponse;
import net.foodeals.offer.application.dtos.responses.SupplementDealResponse;
import net.foodeals.offer.application.services.DealService;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.exceptions.DealNotFoundException;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.offer.domain.repositories.OpenTimeRepository;
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
        User user = userService.getConnectedUser();
        List<SupplementDealResponse> supplementResponses = deal.getSupplements().stream()
                .map(supplement -> new SupplementDealResponse(
                        supplement.getId(),
                        supplement.getName(),
                        supplement.getPrice(),
                        supplement.getSupplementImagePath()
                )).collect(Collectors.toList());
        DealDetailsResponse dealDetailsResponse = new DealDetailsResponse(deal.getId(),
                deal.getTitle(), deal.getProduct().getName(), deal.getProduct().getProductImagePath(),
                deal.getOffer().getSubEntity().getAddress().getAddress() + " "
                        + deal.getOffer().getSubEntity().getAddress().getCity().getName(), deal.getOffer().getPrice().amount(),
                deal.getOffer().getSalePrice().amount(), PriceUtils.calculatePercentageReduction(deal.getOffer().getPrice().amount(),
                deal.getOffer().getSalePrice().amount()), DistanceCalculator.calculateDistance(user.getCoordinates().latitude(),
                user.getCoordinates().longitude(), deal.getOffer().getSubEntity().getCoordinates().latitude(),
                deal.getOffer().getSubEntity().getCoordinates().longitude()), supplementResponses);
    return dealDetailsResponse;
    }
}
