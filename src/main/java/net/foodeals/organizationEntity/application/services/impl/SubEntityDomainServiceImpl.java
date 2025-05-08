package net.foodeals.organizationEntity.application.services.impl;

import lombok.AllArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityDomainResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityProductCategoryResponse;
import net.foodeals.organizationEntity.application.services.SubEntityDomainService;
import net.foodeals.organizationEntity.domain.entities.SubEntityDomain;
import net.foodeals.organizationEntity.domain.entities.SubEntityProductCategory;
import net.foodeals.organizationEntity.domain.repositories.SubEntityDomainRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SubEntityDomainServiceImpl implements SubEntityDomainService {

    private final SubEntityDomainRepository subEntityDomainRepository;

    @Override
    public List<SubEntityDomainResponse> findAll() {
        List<SubEntityDomain> subEntityDomains = subEntityDomainRepository.findAll();
        List<SubEntityDomainResponse> subEntityDomainResponses = new ArrayList<>();
        subEntityDomains.stream().map(subEntityDomain -> new SubEntityDomainResponse(subEntityDomain.getId(), subEntityDomain.getName())).forEach(subEntityDomainResponses::add);
        return subEntityDomainResponses;
    }

    @Override
    public List<SubEntityProductCategoryResponse> findSubEntityProductCategoriesBySubEntityDomain(UUID id) {

        SubEntityDomain subEntityDomain = subEntityDomainRepository.findById(id).orElse(null);
        List<SubEntityProductCategoryResponse> subEntityProductCategories = new ArrayList<>();
        for (SubEntityProductCategory subEntityProductCategory : subEntityDomain.getSubEntityProductCategories()) {
            subEntityProductCategories.add(new SubEntityProductCategoryResponse(subEntityProductCategory.getId(), generatePhotoUrl(subEntityProductCategory.getName()),subEntityProductCategory.getName()));
        }
        return subEntityProductCategories;
    }
    
    private String generatePhotoUrl(String name) {
        String baseUrl = "/images/"; // Votre domaine ou base d'URL
        String formattedName = name.trim().toLowerCase().replace(" ", "-"); // Transformation
        return baseUrl + formattedName + ".jpg"; // Exemple d'extension .jpg
    }

}
