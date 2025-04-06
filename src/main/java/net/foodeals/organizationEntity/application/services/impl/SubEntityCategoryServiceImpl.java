package net.foodeals.organizationEntity.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.services.SubEntityCategoryService;
import net.foodeals.organizationEntity.domain.entities.SubEntityProductCategory;
import net.foodeals.organizationEntity.domain.repositories.SubEntityDomainRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor

public class SubEntityCategoryServiceImpl implements SubEntityCategoryService {


    private final SubEntityDomainRepository subEntityDomainRepository;

    @Override
    public List<SubEntityProductCategory> getCategoriesBySubEntityId(UUID subEntityId) {
        return subEntityDomainRepository.findCategoriesBySubEntityId(subEntityId);
    }

}
