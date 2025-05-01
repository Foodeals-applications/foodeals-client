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
}
