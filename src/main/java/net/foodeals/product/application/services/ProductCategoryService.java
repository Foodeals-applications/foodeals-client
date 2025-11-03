package net.foodeals.product.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.ProductCategory;
import net.foodeals.product.application.dtos.requests.ProductCategoryRequest;

import java.util.UUID;

public interface ProductCategoryService extends CrudService<ProductCategory, UUID, ProductCategoryRequest> {
}
