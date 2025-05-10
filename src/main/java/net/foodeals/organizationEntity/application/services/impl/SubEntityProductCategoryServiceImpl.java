package net.foodeals.organizationEntity.application.services.impl;

import lombok.AllArgsConstructor;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.organizationEntity.application.dtos.responses.CategoryWithDealCountResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityProductCategoryResponse;
import net.foodeals.organizationEntity.application.services.SubEntityProductCategoryService;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntityProductCategory;
import net.foodeals.organizationEntity.domain.repositories.SubEntityProductCategoryRepository;
import net.foodeals.organizationEntity.domain.repositories.SubEntityRepository;

import org.springframework.stereotype.Service;

import com.google.zxing.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SubEntityProductCategoryServiceImpl implements SubEntityProductCategoryService {

    private final SubEntityProductCategoryRepository subEntityProductCategoryRepository;
    private final SubEntityRepository subEntityRepository;
    private final DealRepository dealRepository;
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

    public List<SubEntityProductCategoryResponse> findAllCategoriesBySubEntity(UUID subEntityId) {
        SubEntity subEntity = subEntityRepository.findById(subEntityId).get();
        if(subEntity!=null) {
        return subEntity.getSubEntityDomains().stream()
            .flatMap(domain -> domain.getSubEntityProductCategories().stream())
            .distinct() // si des catégories sont partagées
            .map(cat -> new SubEntityProductCategoryResponse(cat.getId(),generatePhotoUrl(cat.getName()), cat.getName()))
            .collect(Collectors.toList());
        }
        return null;
    }
    
    
    @Override
    public List<CategoryWithDealCountResponse> getCategoriesWithDealCountBySubEntity(UUID subEntityId) {
        SubEntity subEntity = subEntityRepository.findById(subEntityId).get();
        if(subEntity!=null) {
        return subEntity.getSubEntityDomains().stream()
            .flatMap(domain -> domain.getSubEntityProductCategories().stream())
            .distinct()
            .map(category -> {
                long count = dealRepository.countDealsByCategory(category); // Assure-toi que ce repo existe
                return new CategoryWithDealCountResponse(category.getId(),generatePhotoUrl(category.getName()), category.getName(), count);
            })
            .collect(Collectors.toList());
        }
        return null ;
    }

    

}
