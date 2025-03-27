package net.foodeals.product.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.product.application.dtos.requests.ProductSubCategoryRequest;
import net.foodeals.product.domain.entities.ProductSubCategory;

import java.util.UUID;

public interface ProductSubCategoryService extends CrudService<ProductSubCategory, UUID, ProductSubCategoryRequest> {
}
