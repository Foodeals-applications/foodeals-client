package net.foodeals.organizationEntity.Controller;

import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.responses.*;
import net.foodeals.organizationEntity.application.services.SubEntityCategoryService;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntityDomain;
import net.foodeals.organizationEntity.domain.entities.SubEntityProductCategory;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/subentities")
@RequiredArgsConstructor
public class SubEntityController {

    private final SubEntityService subEntityService;

    private final UserService userService;

    private final SubEntityCategoryService subEntityCategoryService;


    @GetMapping("/{id}/details")
    public ResponseEntity<SubEntityDetailsResponse> getSubEntityDetails(@PathVariable UUID id) {
        Integer userId = userService.getConnectedUser().getId();
        SubEntityDetailsResponse response = subEntityService.getSubEntityDetails(id, userId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/domains/{subEntityId}")
    public ResponseEntity<List<SubEntityDomain>> getDomainsBySubEntity(
            @PathVariable UUID subEntityId) {
        SubEntity subEntity = subEntityService.findById(subEntityId);
        List<SubEntityDomain> response = subEntity.getSubEntityDomains();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-sub-entity/{subEntityId}")
    public ResponseEntity<List<SubEntityProductCategoryResponse>> getSubEntityCategoriesBySubEntityId(
            @PathVariable UUID subEntityId) {
        List<SubEntityProductCategory> categories = subEntityCategoryService.getCategoriesBySubEntityId(subEntityId);
        List<SubEntityProductCategoryResponse> response = categories.stream()
                .map(c -> new SubEntityProductCategoryResponse(c.getId(), c.getName(), generatePhotoUrl(c.getName())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BestSellerResponse>> getBestSellers(
            @RequestParam(value = "threshold", defaultValue = "1000") Double salesThreshold
    ) {
        List<BestSellerResponse> bestSellers = subEntityService.getBestSellers(salesThreshold);
        return ResponseEntity.ok(bestSellers);
    }

    @GetMapping("/spotlight")
    public List<SpotlightStore> getSpotlightStores() {
        return subEntityService.getSpotlightStores();
    }

    @GetMapping("/hotel/details/{id}")
    public ResponseEntity<HotelDetailsResponse> getHotemDetails(@PathVariable UUID id) {

        HotelDetailsResponse response = subEntityService.getHotelDetails(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurant/details/{id}")
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantDetails(@PathVariable UUID id) {
        RestaurantDetailsResponse response = subEntityService.getRestaurantDetails(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bakery/details/{id}")
    public ResponseEntity<BakeryDetailsResponse> getBakeryDetails(@PathVariable UUID id) {
        BakeryDetailsResponse response = subEntityService.getBakeryDetails(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/industry/details/{id}")
    public ResponseEntity<IndustryDetailsResponse> getIndustryDetails(@PathVariable UUID id) {
        IndustryDetailsResponse response = subEntityService.getIndustryDetails(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/agriculture/details/{id}")
        public ResponseEntity<AgriculturDetailsResponse> getAgricultureDetails(@PathVariable UUID id) {
        AgriculturDetailsResponse response = subEntityService.getAgricultureDetails(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurant/list")
    public ResponseEntity<List<RestaurantResponse>> getListRestaurant() {
        User connectedUser = userService.getConnectedUser();
        List<RestaurantResponse>response=subEntityService.getListOfRestaurants(connectedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hotel/list")
    public ResponseEntity<List<HotelResponse>> getListHotels() {
        User connectedUser = userService.getConnectedUser();
        List<HotelResponse>response=subEntityService.getListOfHotels(connectedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bakery/list")
    public ResponseEntity<List<BakeryResponse>> getListBakeries() {
        User connectedUser = userService.getConnectedUser();
        List<BakeryResponse>response=subEntityService.getListOfBakeries(connectedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/agriculture/list")
    public ResponseEntity<List<AgricultureResponse>> getListAgricultures() {
        User connectedUser = userService.getConnectedUser();
        List<AgricultureResponse>response=subEntityService.getListOfAgrucultures(connectedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stores/map")
    public List<StoreMapDTO> getStoresOnMap() {
        return subEntityService.getStoresOnMap();
    }
    
    private String generatePhotoUrl(String name) {
        String baseUrl = "/images/"; // Votre domaine ou base d'URL
        String formattedName = name.trim().toLowerCase().replace(" ", "-"); // Transformation
        return baseUrl + formattedName + ".jpg"; // Exemple d'extension .jpg
    }


}
