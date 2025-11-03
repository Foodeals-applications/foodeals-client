package net.foodeals.product.application.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.*;
import net.foodeals.core.repositories.*;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.application.dtos.requests.ProductReviewRequest;
import net.foodeals.product.application.dtos.responses.*;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.user.application.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
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