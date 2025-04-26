package net.foodeals.organizationEntity.Controller;

import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.responses.*;
import net.foodeals.organizationEntity.application.services.SubEntityCategoryService;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.organizationEntity.domain.entities.SubEntityProductCategory;
import net.foodeals.user.application.services.UserService;
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

    private String generatePhotoUrl(String name) {
        String baseUrl = "/images/"; // Votre domaine ou base d'URL
        String formattedName = name.trim().toLowerCase().replace(" ", "-"); // Transformation
        return baseUrl + formattedName + ".jpg"; // Exemple d'extension .jpg
    }


}
