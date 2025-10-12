package net.foodeals.filters.application.services;


import lombok.RequiredArgsConstructor;
import net.foodeals.common.Utils.DistanceCalculator;
import net.foodeals.filters.application.dtos.GlobalSearchResponse;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.order.domain.repositories.DeliveryOptionRepository;
import net.foodeals.organizationEntity.application.dtos.responses.StoreResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityResponse;
import net.foodeals.organizationEntity.domain.repositories.SubEntityRepository;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductRepository productRepository;
    private final SubEntityRepository storeRepository;
    private final DealRepository dealRepository;

    private final DeliveryOptionRepository deliveryMethodRepository;

    public Map<String, Object> getSuggestions(String query) {
        List<String> suggestions = productRepository.findTop10ByNameContainingIgnoreCase(query).stream().map(p -> p.getName()).toList();
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
        var methods = deliveryMethodRepository.findAll().stream().map(m -> Map.of("id", m.getId(), "label", m.getLabel())).toList();
        return Map.of("deliveryMethods", methods);
    }


    public Map<String, Object> getQuantitySizes() {
        return Map.of("sizes", List.of(Map.of("id", "small", "label", "Petite quantité", "description", "Moins de 10 unités"), Map.of("id", "medium", "label", "Moyenne quantité", "description", "Entre 10 et 50 unités"), Map.of("id", "large", "label", "Grande quantité", "description", "Plus de 50 unités")));
    }

    public Map<String, Object> getCommerceCategories() {
        return Map.of("commerceTypes", List.of(Map.of("id", "restaurant", "name", "Restaurant", "icon", "🍽️"), Map.of("id", "bakery", "name", "Boulangerie", "icon", "🥖"), Map.of("id", "grocery", "name", "Épicerie", "icon", "🛒")));
    }

    public Map<String, Object> getDateOptions() {
        return Map.of("dateOptions", List.of(Map.of("id", "today", "label", "Aujourd'hui", "type", "day"), Map.of("id", "week", "label", "Cette semaine", "type", "week"), Map.of("id", "month", "label", "Ce mois", "type", "month")));
    }

    public Map<String, Object> getPriceRange() {
        return Map.of("minPrice", 0, "maxPrice", 1000, "step", 10, "currency", "EUR");
    }

    public List<Map<String, Object>> getFoodCategories() {
        return List.of(
                Map.of("id", "fruit", "name", "Fruits", "icon", "🍎"),
                Map.of("id", "vegetable", "name", "Légumes", "icon", "🥦"),
                Map.of("id", "meat", "name", "Viandes", "icon", "🍖"),
                Map.of("id", "bakery", "name", "Boulangerie", "icon", "🥐"),
                Map.of("id", "dairy", "name", "Produits laitiers", "icon", "🧀"),
                Map.of("id", "beverage", "name", "Boissons", "icon", "🥤"),
                Map.of("id", "prepared", "name", "Plats préparés", "icon", "🍲")
        );
    }
    public List<Map<String, Object>> getProductTypes() {
        return List.of(
                Map.of("id", "fresh", "name", "Frais", "description", "Produits réfrigérés ou périssables"),
                Map.of("id", "dry", "name", "Sec", "description", "Produits secs, conserves, etc."),
                Map.of("id", "frozen", "name", "Surgelé", "description", "Produits congelés"),
                Map.of("id", "organic", "name", "Bio", "description", "Certifiés agriculture biologique"),
                Map.of("id", "vegan", "name", "Vegan", "description", "Sans produits d’origine animale")
        );
    }

    public GlobalSearchResponse globalSearch(String q, String type, double lat, double lng) {

        boolean searchStores = type.equalsIgnoreCase("all") || type.equalsIgnoreCase("store");
        boolean searchProducts = type.equalsIgnoreCase("all") || type.equalsIgnoreCase("product");
        boolean searchDeals = type.equalsIgnoreCase("all") || type.equalsIgnoreCase("deal");

        List<StoreResponse> stores = List.of();
        List<ProductResponse> products = List.of();
        List<DealResponse> deals = List.of();

        if (searchStores) {
            stores = storeRepository.searchByNameContainingIgnoreCase(q).stream()
                    .map(store -> {
                        double distance = DistanceCalculator.calculateDistance(
                                lat, lng,
                                store.getCoordinates().latitude().doubleValue(),
                                store.getCoordinates().longitude().doubleValue()
                        );
                        return new StoreResponse(
                                store.getId(),
                                store.getName(),
                                store.getAvatarPath(),
                                store.getCoverPath(),
                                distance,
                                store.getNumberOfLikes(),
                                store.getNumberOfStars()

                        );
                    })
                    .collect(Collectors.toList());
        }

        if (searchProducts) {
            products = productRepository.searchByNameContainingIgnoreCase(q).stream()
                    .map(ProductResponse::fromEntity)
                    .collect(Collectors.toList());
        }

        if (searchDeals) {
            deals = dealRepository.searchByDescriptionContainingIgnoreCase(q).stream()
                    .map(DealResponse::fromEntity)
                    .collect(Collectors.toList());
        }

        return new GlobalSearchResponse(stores, products, deals);
    }


}

