package net.foodeals.product.application.services;

import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.product.application.dtos.requests.ProductBrandRequest;
import net.foodeals.product.domain.entities.ProductBrand;

public interface ProductBrandService extends CrudService<ProductBrand, UUID, ProductBrandRequest> {

	public ProductBrand findByName(String name);
}
