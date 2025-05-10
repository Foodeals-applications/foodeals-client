package net.foodeals.organizationEntity.application.services;

import java.util.List;
import java.util.UUID;

import net.foodeals.organizationEntity.application.dtos.responses.CategoryWithDealCountResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityProductCategoryResponse;

public interface SubEntityProductCategoryService {

    List<SubEntityProductCategoryResponse> findBySubEntityDomainId(UUID domainId);
    List<SubEntityProductCategoryResponse> findAll();
    List<SubEntityProductCategoryResponse> findAllCategoriesBySubEntity(UUID subEntityId);
    List<CategoryWithDealCountResponse> getCategoriesWithDealCountBySubEntity(UUID subEntityId);
}
