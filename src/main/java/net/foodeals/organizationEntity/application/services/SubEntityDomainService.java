package net.foodeals.organizationEntity.application.services;

import net.foodeals.organizationEntity.application.dtos.responses.SubEntityDomainResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityProductCategoryResponse;
import net.foodeals.organizationEntity.domain.entities.SubEntityDomain;

import java.util.List;
import java.util.UUID;

public interface SubEntityDomainService {

    List<SubEntityDomainResponse> findAll();

    List<SubEntityProductCategoryResponse>findSubEntityProductCategoriesBySubEntityDomain(UUID id);
}
