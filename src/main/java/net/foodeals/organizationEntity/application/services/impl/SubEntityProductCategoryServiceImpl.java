package net.foodeals.organizationEntity.application.services.impl;

import lombok.AllArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityProductCategoryResponse;
import net.foodeals.organizationEntity.application.services.SubEntityProductCategoryService;
import net.foodeals.organizationEntity.domain.entities.SubEntityProductCategory;
import net.foodeals.organizationEntity.domain.repositories.SubEntityProductCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SubEntityProductCategoryServiceImpl implements SubEntityProductCategoryService {

    private final SubEntityProductCategoryRepository subEntityProductCategoryRepository;

    @Override
    public List<SubEntityProductCategoryResponse> findBySubEntityDomainId(UUID domainId) {
        List<SubEntityProductCategory> subEntityProductCategories = subEntityProductCategoryRepository.findBySubEntityDomainId(domainId);
        List<SubEntityProductCategoryResponse> subEntityProductCategoryResponses = new ArrayList<>();
        for (SubEntityProductCategory subEntityProductCategory : subEntityProductCategories) {
            SubEntityProductCategoryResponse subEntityProductCategoryResponse = new SubEntityProductCategoryResponse();
            subEntityProductCategoryResponse.setId(subEntityProductCategory.getId());
            subEntityProductCategoryResponse.setName(subEntityProductCategory.getName());
        }
        return subEntityProductCategoryResponses;
        
     
    }

    @Override
    public List<SubEntityProductCategoryResponse> findAll() {
        return subEntityProductCategoryRepository.findAll().stream()
            .collect(Collectors.collectingAndThen(
                Collectors.toMap(
                    category -> category.getName(), // clé : nom
                    category -> category,          // valeur
                    (c1, c2) -> c1                 // en cas de doublon, garder le 1er
                ),
                map -> map.values().stream()
                    .map(category -> new SubEntityProductCategoryResponse(
                        category.getId(),
                        generatePhotoUrl(category.getName()),
                        category.getName()
                    ))
                    .collect(Collectors.toList())
            ));
    }
    
    private String generatePhotoUrl(String name) {
        String baseUrl = "/images/"; // Votre domaine ou base d'URL
        String formattedName = name.trim().toLowerCase().replace(" ", "-"); // Transformation
        return baseUrl + formattedName + ".jpg"; // Exemple d'extension .jpg
    }
    
    

}
