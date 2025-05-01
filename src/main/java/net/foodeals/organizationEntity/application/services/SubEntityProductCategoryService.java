package net.foodeals.organizationEntity.application.services;

import net.foodeals.organizationEntity.application.dtos.responses.SubEntityProductCategoryResponse;
import net.foodeals.organizationEntity.domain.entities.SubEntityProductCategory;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubEntityProductCategoryService {

    List<SubEntityProductCategoryResponse> findBySubEntityDomainId(UUID domainId);
}
