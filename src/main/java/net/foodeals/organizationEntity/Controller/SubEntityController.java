package net.foodeals.organizationEntity.Controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import net.foodeals.organizationEntity.application.dtos.responses.BestSellerResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityDetailsResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityProductCategoryResponse;
import net.foodeals.organizationEntity.application.services.SubEntityCategoryService;
import net.foodeals.organizationEntity.domain.entities.SubEntityProductCategory;
import net.foodeals.user.application.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.services.SubEntityService;

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
                .map(c -> new SubEntityProductCategoryResponse(c.getId(), c.getName(),generatePhotoUrl(c.getName())))
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



    private String generatePhotoUrl(String name) {
        String baseUrl = "/images/"; // Votre domaine ou base d'URL
        String formattedName = name.trim().toLowerCase().replace(" ", "-"); // Transformation
        return baseUrl + formattedName + ".jpg"; // Exemple d'extension .jpg
    }


}
