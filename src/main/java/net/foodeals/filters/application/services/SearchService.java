package net.foodeals.filters.application.services;


import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.domain.repositories.SubEntityDomainRepository;
import net.foodeals.organizationEntity.domain.repositories.SubEntityRepository;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.repositories.DeliveryMethodRepository;
import net.foodeals.product.domain.repositories.ProductCategoryRepository;
import net.foodeals.product.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductRepository productRepository;
    private final SubEntityRepository
            storeRepository;

    private final DeliveryMethodRepository deliveryMethodRepository;
    private final ProductCategoryRepository foodCategoryRepository;
    private final SubEntityDomainRepository subEntityDomainRepository;




    public Map<String, Object> getSuggestions(String query) {
        List<String> suggestions = productRepository.findTop10ByNameContainingIgnoreCase(query)
                .stream().map(p -> p.getName()).toList();
        return Map.of("suggestions", suggestions);
    }

    public Map<String, Object> getRecentSearches() {
        return Map.of("recentSearches", List.of("pizza", "bio fruits", "local honey"));
    }

    public Map<String, Object> saveSearch(Map<String, Object> body) {
        return Map.of("success", true, "query", body.get("query"));
    }

    public Map<String, Object> deleteRecent(String query) {
        return Map.of("success", true, "deletedQuery", query);
    }

    public Map<String, Object> getTrendingSearches() {
        return Map.of("trendingSearches", List.of("organic eggs", "vegan burgers", "local cheese"));
    }

    public Map<String, Object> globalSearch(String q, String type, String location) {
        var stores = storeRepository.findTop5ByNameContainingIgnoreCase(q);
        var products = productRepository.findTop10ByNameContainingIgnoreCase(q);
        return Map.of("stores", stores, "products", products, "deals", List.of());
    }


    public Map<String, Object> getDeliveryMethods() {
        var methods = deliveryMethodRepository.findAll()
                .stream()
                .map(m -> Map.of(
                        "id", m.getId(),
                        "label", m.getDeliveryName()
                ))
                .toList();
        return Map.of("deliveryMethods", methods);
    }

    public Map<String, Object> getFoodCategories() {
        var categories = foodCategoryRepository.findAll()
                .stream()
                .map(c -> Map.of(
                        "id", c.getId(),
                        "name", c.getName()
                                        ))
                .toList();
        return Map.of("categories", categories);
    }

    public Map<String, Object> getProductTypes() {
        var types = productRepository.findAll()
                .stream()
                .map(t -> Map.of(
                        "id", t.getId(),
                        "name", t.getName(),
                        "description", t.getDescription()
                ))
                .toList();
        return Map.of("types", types);
    }



    public Map<String, Object> getCommerceCategories() {
        var commerceTypes = subEntityDomainRepository.findAll()
                .stream()
                .map(c -> Map.of(
                        "id", c.getId(),
                        "name", c.getName()
                ))
                .toList();
        return Map.of("commerceTypes", commerceTypes);
    }
}
