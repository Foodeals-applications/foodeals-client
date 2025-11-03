package net.foodeals.product.application.services;


import java.util.List;
import java.util.UUID;


import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.Product;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.application.dtos.requests.ProductReviewRequest;
import net.foodeals.product.application.dtos.responses.*;


public interface ProductService extends CrudService<Product, UUID, ProductRequest> {


    public ProductResponse getProductDetails(UUID productId);

    public List<ProductSuggestionResponse> getSimilarProducts(Product product);

    public ProductStoreDetailResponse getProductWithRelated(UUID productId);

    public List<ProductStoreResponse> getProductsByStore(UUID subEntityId);

    public ProductReviewResponse addReview(UUID productId, ProductReviewRequest request);


}

