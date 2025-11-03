package net.foodeals.home.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.Utils.DistanceCalculator;
import net.foodeals.core.domain.entities.Box;
import net.foodeals.core.domain.entities.Deal;
import net.foodeals.core.domain.entities.SubEntity;
import net.foodeals.core.domain.entities.User;
import net.foodeals.core.repositories.SubEntityRepository;
import net.foodeals.home.application.dtos.HomeContentResponse;
import net.foodeals.home.application.dtos.HomeFilteredContent;
import net.foodeals.home.application.dtos.HomeFilters;
import net.foodeals.organizationEntity.application.dtos.responses.*;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.user.application.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeContentService {

    private final SubEntityRepository subEntityRepository;
    private final SubEntityService subEntityService;
    private final UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public HomeFilters decodeFilters(String filters) {
        try {
            return objectMapper.readValue(filters, HomeFilters.class);
        } catch (Exception e) {
            return new HomeFilters(); // fallback si rien
        }
    }

    public HomeContentResponse getFilteredContent(String filters) {
        User user = userService.getConnectedUser();
        HomeFilters decodedFilters = decodeFilters(filters);

        double userLat = user.getCoordinates().latitude().doubleValue();
        double userLng = user.getCoordinates().longitude().doubleValue();

        // Restaurants
        List<RestaurantResponse> restaurants = subEntityRepository.findByDomaineName("Restaurants").stream()
                .map(subEntity -> {
                    double distance = DistanceCalculator.calculateDistance(
                            userLat, userLng,
                            subEntity.getCoordinates().latitude().doubleValue(),
                            subEntity.getCoordinates().longitude().doubleValue()
                    );
                    if (!applyFilters(subEntity, distance, decodedFilters)) return null;
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
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Bakeries
        List<BakeryResponse> bakeries = subEntityRepository.findByDomaineName("Pâtisseries").stream()
                .map(subEntity -> {
                    double distance = DistanceCalculator.calculateDistance(
                            userLat, userLng,
                            subEntity.getCoordinates().latitude().doubleValue(),
                            subEntity.getCoordinates().longitude().doubleValue()
                    );
                    if (!applyFilters(subEntity, distance, decodedFilters)) return null;
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
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Hotels
        List<HotelResponse> hotels = subEntityRepository.findByDomaineName("Hôtels").stream()
                .map(subEntity -> {
                    double distance = DistanceCalculator.calculateDistance(
                            userLat, userLng,
                            subEntity.getCoordinates().latitude().doubleValue(),
                            subEntity.getCoordinates().longitude().doubleValue()
                    );
                    if (!applyFilters(subEntity, distance, decodedFilters)) return null;
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
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Farmers
        List<AgricultureResponse> farmers = subEntityRepository.findByDomaineName("Agricultures").stream()
                .map(subEntity -> {
                    double distance = DistanceCalculator.calculateDistance(
                            userLat, userLng,
                            subEntity.getCoordinates().latitude().doubleValue(),
                            subEntity.getCoordinates().longitude().doubleValue()
                    );
                    if (!applyFilters(subEntity, distance, decodedFilters)) return null;
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
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Deals + Boxes (idem : appliquer filters par prix, distance si besoin)
        List<Deal> deals = List.of(); // TODO: brancher sur repo Offer + appliquer filters.getMaxPrice()
        List<Box> boxes = List.of();

        List<SubEntity> industrials = List.of();
        List<BestSellerResponse> bestSellers =  subEntityService.getBestSellers(Double.parseDouble("10"));

        HomeFilteredContent content = new HomeFilteredContent(
                deals,
                restaurants,
                bakeries,
                hotels,
                boxes,
                farmers,
                industrials,
                bestSellers
        );

        return new HomeContentResponse(content, false);
    }


    private boolean applyFilters(SubEntity subEntity, double distance, HomeFilters filters) {
        if (filters.getMaxDistance() != null && distance > filters.getMaxDistance()) {
            return false;
        }
        if (filters.getMinStars() != null &&
                (subEntity.getNumberOfStars() == null || subEntity.getNumberOfStars() < filters.getMinStars())) {
            return false;
        }
        if (filters.getMinLikes() != null &&
                (subEntity.getNumberOfLikes() == null || subEntity.getNumberOfLikes() < filters.getMinLikes())) {
            return false;
        }
        return true;
    }
}