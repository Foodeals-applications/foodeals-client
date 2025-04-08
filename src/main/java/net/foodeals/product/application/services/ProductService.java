package net.foodeals.product.application.services;


import java.util.UUID;


import net.foodeals.common.contracts.CrudService;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.domain.entities.Product;


public interface ProductService extends CrudService<Product, UUID, ProductRequest> {


    public ProductResponse getProductDetails(UUID productId);


}

