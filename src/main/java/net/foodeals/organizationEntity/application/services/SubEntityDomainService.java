package net.foodeals.organizationEntity.application.services;

import java.util.List;
import java.util.UUID;

import net.foodeals.organizationEntity.application.dtos.responses.SubEntityDomainResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityProductCategoryResponse;

public interface SubEntityDomainService {

    List<SubEntityDomainResponse> findAll();

    List<SubEntityProductCategoryResponse>findSubEntityProductCategoriesBySubEntityDomain(UUID id);
    
   
}
