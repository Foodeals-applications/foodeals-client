package net.foodeals.organizationEntity.application.services;

import net.foodeals.core.domain.entities.SubEntityProductCategory;

import java.util.List;
import java.util.UUID;

public interface SubEntityCategoryService {

    public List<SubEntityProductCategory> getCategoriesBySubEntityId(UUID subEntityId) ;

}
