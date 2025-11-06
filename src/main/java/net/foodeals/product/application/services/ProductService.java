package net.foodeals.product.application.services;

import java.util.List;
import java.util.UUID;


import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.Product;
import net.foodeals.organizationEntity.application.dtos.requests.AdvancedFilterRequest;
import net.foodeals.organizationEntity.application.dtos.responses.SearchResponse;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.application.dtos.requests.ProductReviewRequest;
import net.foodeals.product.application.dtos.responses.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService extends CrudService<Product, UUID, ProductRequest> {


    public ProductResponse getProductDetails(UUID productId);

    public List<ProductSuggestionResponse> getSimilarProducts(Product product);

    public ProductStoreDetailResponse getProductWithRelated(UUID productId);

    public List<ProductStoreResponse> getProductsByStore(UUID subEntityId);

    public ProductReviewResponse addReview(UUID productId, ProductReviewRequest request);

    SearchResponse searchProducts(UUID storeId, String q, UUID categoryId, UUID domainId, Pageable pageable);

    Page<ProductSearchedDto> filterProducts(UUID storeId, List<UUID> categoryIds, UUID domainId, Double priceMin, Double priceMax, Boolean onlyAvailable, String sortBy, String sortOrder, Pageable pageable);

    SearchResponse advancedFilter(UUID storeId, AdvancedFilterRequest request);
    CategorizedProductsResponse getCategorizedProducts(UUID storeId, UUID domainId, String searchQuery);


}

