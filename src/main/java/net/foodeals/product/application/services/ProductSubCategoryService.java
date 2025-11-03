package net.foodeals.product.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.ProductSubCategory;
import net.foodeals.product.application.dtos.requests.ProductSubCategoryRequest;

import java.util.UUID;

public interface ProductSubCategoryService extends CrudService<ProductSubCategory, UUID, ProductSubCategoryRequest> {
}
