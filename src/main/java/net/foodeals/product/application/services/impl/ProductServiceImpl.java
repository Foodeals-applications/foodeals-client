package net.foodeals.product.application.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.*;
import net.foodeals.core.repositories.*;
import net.foodeals.organizationEntity.application.dtos.requests.AdvancedFilterRequest;
import net.foodeals.organizationEntity.application.dtos.responses.SearchResponse;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.application.dtos.requests.ProductReviewRequest;
import net.foodeals.product.application.dtos.responses.*;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.product.application.specs.ProductSpecs;
import net.foodeals.user.application.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final UserService userService;

    private final ProductCategoryRepository categoryRepository;

    private final DealRepository dealRepository;

    private final SubEntityRepository subEntityRepository;

    private final ReviewRepository reviewRepository;


    public ProductResponse getProductDetails(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Produit non trouvé"));

        // Récupérer les catégories liées
        List<String> categories = categoryRepository.findByProduct(productId)
                .stream()
                .map(ProductCategory::getName)
                .collect(Collectors.toList());
        Deal deal = dealRepository.findActiveDealByProduct(product.getId()).get();
        return ProductResponse.builder()
                .id(product.getId())
                .image(product.getProductImagePath())
                .name(product.getName())
                .description(product.getDescription())
                .price(new PriceResponse(deal.getOffer().getSalePrice().amount().doubleValue(),
                        deal.getOffer().getPrice().amount().doubleValue()))
                .categories(categories)
                .stock(product.getStock())
                .subEntityId(product.getSubEntity().getId())
                .build();
    }

    @Override
    public List<ProductSuggestionResponse> getSimilarProducts(Product product) {
        List<Product> similarProducts = productRepository
                .findByCategoryAndIdNot(product.getCategory(), product.getId());

        // Récupérer les IDs de tous les produits similaires
        List<UUID> productIds = similarProducts.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        // Charger tous les deals actifs en une seule requête
        List<Deal> activeDeals = dealRepository.findActiveDealsByProductIds(productIds);

        // Mapper les deals par ID produit
        Map<UUID, Deal> dealMap = activeDeals.stream()
                .collect(Collectors.toMap(deal -> deal.getProduct().getId(), deal -> deal));

        return similarProducts.stream()
                .filter(p -> dealMap.containsKey(p.getId())) // Garder seulement les produits avec un deal
                .map(p -> {
                    Deal deal = dealMap.get(p.getId());
                    double oldPrice = deal.getOffer().getPrice().amount().doubleValue();
                    double newPrice = deal.getOffer().getSalePrice().amount().doubleValue();
                    PriceResponse price = new PriceResponse(oldPrice, newPrice);
                    return new ProductSuggestionResponse(
                            p.getId(),
                            p.getProductImagePath(),
                            p.getName(),
                            price,
                            p.getStock()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public Page<Product> findAll(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public Product findById(UUID uuid) {
        return productRepository.findById(uuid).orElse(null);
    }

    @Override
    public Product create(ProductRequest dto) {
        return null;
    }

    @Override
    public Product update(UUID uuid, ProductRequest dto) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }


    public ProductStoreDetailResponse getProductWithRelated(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductStoreResponse mainProduct = mapToResponse(product);

        List<ProductStoreResponse> relatedProducts = productRepository
                .findBySubEntityAndIdNot(product.getSubEntity(), product.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new ProductStoreDetailResponse(mainProduct, relatedProducts);
    }

    /**
     * Récupérer tous les produits d’un store (subEntity).
     */
    public List<ProductStoreResponse> getProductsByStore(UUID subEntityId) {
        SubEntity subEntity = subEntityRepository.findById(subEntityId).get();
        List<ProductStoreResponse> products = productRepository.findBySubEntity(subEntity)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return products;
    }

    @Override
    public ProductReviewResponse addReview(UUID productId, ProductReviewRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        User currentUser = userService.getConnectedUser(); // ou null si anonyme autorisé

        Review review = Review.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .product(product)
                .user(currentUser)
                .build();

        Review saved = reviewRepository.save(review);

        return new ProductReviewResponse(
                saved.getId(),
                saved.getRating(),
                saved.getComment(),
                product.getId(),
                saved.getCreatedAt()
        );
    }

    @Override
    public SearchResponse searchProducts(UUID storeId, String q, UUID categoryId, UUID domainId, Pageable pageable) {
        if (q == null || q.trim().length() < 2) {
            return new SearchResponse(List.of(), 0, pageable.getPageSize(), pageable.getPageNumber());
        }

        SubEntity store = subEntityRepository.findById(storeId).orElse(null);
        if (store == null) {
            throw new EntityNotFoundException("Store not found: " + storeId);
        }

        Page<Product> page = productRepository.searchInStore(storeId, q, pageable);
        List<ProductSearchedDto> dto = page.getContent()
                .stream()
                .map(p -> toDto(p, store))
                .collect(Collectors.toList());

        return new SearchResponse(dto, page.getTotalElements(), pageable.getPageSize(), pageable.getPageNumber());
    }

    @Override
    public Page<ProductSearchedDto> filterProducts(UUID storeId, List<UUID> categoryIds, UUID domainId,
                                                   Double priceMin, Double priceMax, Boolean onlyAvailable,
                                                   String sortBy, String sortOrder, Pageable pageable) {

        Specification<Product> spec = Specification.where(ProductSpecs.inStore(storeId));

        if (categoryIds != null && !categoryIds.isEmpty())
            spec = spec.and(ProductSpecs.categoryIn(categoryIds));

        if (domainId != null)
            spec = spec.and(ProductSpecs.domainEquals(domainId));

        if (priceMin != null && priceMax != null)
            spec = spec.and(ProductSpecs.priceBetween(
                    BigDecimal.valueOf(priceMin), BigDecimal.valueOf(priceMax)));

        if (Boolean.TRUE.equals(onlyAvailable))
            spec = spec.and(ProductSpecs.onlyAvailable(true));

        Sort sort = Sort.by(
                Sort.Direction.fromString(sortOrder == null ? "DESC" : sortOrder.toUpperCase()),
                sortBy == null ? "name" : sortBy
        );

        Pageable p = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Product> page = productRepository.findAll(spec, p);
        return page.map(product -> toDto(product, product.getSubEntity()));
    }

    @Override
    public SearchResponse advancedFilter(UUID storeId, AdvancedFilterRequest request) {
        int limit = Optional.ofNullable(request.pagination())
                .map(AdvancedFilterRequest.Pagination::limit)
                .orElse(20);
        int offset = Optional.ofNullable(request.pagination())
                .map(AdvancedFilterRequest.Pagination::offset)
                .orElse(0);

        Pageable p = PageRequest.of(offset / limit, limit);

        Specification<Product> spec = Specification.where(ProductSpecs.inStore(storeId));

        if (request.searchQuery() != null && !request.searchQuery().isBlank())
            spec = spec.and(ProductSpecs.nameLike(request.searchQuery()));

        if (request.categoryIds() != null && !request.categoryIds().isEmpty())
            spec = spec.and(ProductSpecs.categoryIn(request.categoryIds()));

        if (request.domainId() != null)
            spec = spec.and(ProductSpecs.domainEquals(request.domainId()));

        if (request.priceRange() != null)
            spec = spec.and(ProductSpecs.priceBetween(
                    request.priceRange().min(), request.priceRange().max()));

        if (request.filters() != null && Boolean.TRUE.equals(request.filters().onlyAvailable()))
            spec = spec.and(ProductSpecs.onlyAvailable(true));

        Page<Product> page = productRepository.findAll(spec, p);
        List<ProductSearchedDto> dto = page.getContent()
                .stream()
                .map(prod -> toDto(prod, prod.getSubEntity()))
                .collect(Collectors.toList());

        return new SearchResponse(dto, page.getTotalElements(), limit, offset);
    }

    @Override
    public CategorizedProductsResponse getCategorizedProducts(UUID storeId, UUID domainId, String searchQuery) {
        List<Object[]> raws = categoryRepository.findCategoriesWithCountsRaw(storeId);
        List<CategorizedProductsDto> cats = new ArrayList<>();
        long totalProducts = 0;

        for (Object[] row : raws) {
            UUID catId = (UUID) row[0];
            String name = (String) row[1];
            String description = (String) row[2];
            String image = (String) row[3];
            Long itemsCount = (Long) row[4];

            Specification<Product> spec = Specification.where(ProductSpecs.inStore(storeId))
                    .and((root, query, cb) -> cb.equal(root.get("category").get("id"), catId));

            if (domainId != null)
                spec = spec.and(ProductSpecs.domainEquals(domainId));

            if (searchQuery != null && !searchQuery.isBlank())
                spec = spec.and(ProductSpecs.nameLike(searchQuery));

            List<Product> products = productRepository.findAll(spec, PageRequest.of(0, 50)).getContent();
            List<ProductSearchedDto> prodDtos = products.stream()
                    .map(p -> toDto(p, p.getSubEntity()))
                    .collect(Collectors.toList());

            cats.add(new CategorizedProductsDto(name, catId, image, prodDtos));
            totalProducts += prodDtos.size();
        }

        return new CategorizedProductsResponse(cats, cats.size(), totalProducts);
    }

    private ProductSearchedDto toDto(Product p, SubEntity store) {
        return new ProductSearchedDto(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getProductImagePath(),
                p.getPrice() != null ? p.getPrice().amount() : null,
                p.getPrice() != null ? p.getPrice().amount() : null,
                p.getStock(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getStock() != null && p.getStock() > 0,
                calculateDiscount(p).intValue(),
                store != null && store.getNumberOfStars() != null
                        ? store.getNumberOfStars().doubleValue() : 0.0,
               10
        );
    }

    private Double calculateDiscount(Product p) {
        if (p.getPrice() == null || p.getPrice() == null) return null;
        BigDecimal oldPrice = p.getPrice().amount();
        BigDecimal newPrice = p.getPrice().amount();
        if (oldPrice.compareTo(BigDecimal.ZERO) <= 0) return null;
        return 100 * (oldPrice.doubleValue() - newPrice.doubleValue()) / oldPrice.doubleValue();
    }

    /**
     * Mapper Product vers ProductResponse DTO.
     */
    private ProductStoreResponse mapToResponse(Product product) {
        return new ProductStoreResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice().amount(),
                product.getPrice() != null ? product.getPrice().amount() : null,
                product.getProductImagePath(),
                product.getCategory().getName(),
                product.getSubEntity().getId(),
                true
        );
    }


}