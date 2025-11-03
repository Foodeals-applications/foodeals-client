package net.foodeals.filters.application.services;


import lombok.RequiredArgsConstructor;
import net.foodeals.common.Utils.DistanceCalculator;
import net.foodeals.core.domain.entities.SubEntity;
import net.foodeals.core.repositories.DealRepository;
import net.foodeals.core.repositories.DeliveryOptionRepository;
import net.foodeals.core.repositories.ProductRepository;
import net.foodeals.core.repositories.SubEntityRepository;
import net.foodeals.filters.application.dtos.DomainResponse;
import net.foodeals.filters.application.dtos.GlobalSearchResponse;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.organizationEntity.application.dtos.responses.StoreResponse;
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

    public GlobalSearchResponse globalFilter(
            String q, String price, String category, String distanceStr, String ratingStr,
            double lat, double lng
    ) {
        double maxDistance = distanceStr != null ? Double.parseDouble(distanceStr) : 9999;
        double minRating = ratingStr != null ? Double.parseDouble(ratingStr) : 0;

        double minPrice = 0, maxPrice = Double.MAX_VALUE;
        if (price != null && price.contains("-")) {
            String[] parts = price.split("-");
            minPrice = Double.parseDouble(parts[0]);
            maxPrice = Double.parseDouble(parts[1]);
        }

        // 🏬 1️⃣ — Récupération et filtrage des SubEntities
        List<SubEntity> allSubEntities = storeRepository.findAll().stream()
                .filter(sub -> q == null || sub.getName().toLowerCase().contains(q.toLowerCase()))
                .filter(sub -> sub.getCoordinates() != null)
                .filter(sub -> {
                    double distance = DistanceCalculator.calculateDistance(
                            lat, lng,
                            sub.getCoordinates().latitude().doubleValue(),
                            sub.getCoordinates().longitude().doubleValue()
                    );
                    return distance <= maxDistance;
                })
                .filter(sub -> sub.getNumberOfStars() == null || sub.getNumberOfStars() >= minRating)
                .collect(Collectors.toList());

        // 🍽️ 2️⃣ — Catégories principales
        List<StoreResponse> restaurants = filterByCategory(allSubEntities, "restaurant", lat, lng);
        List<StoreResponse> boulangeries = filterByCategory(allSubEntities, "boulangerie", lat, lng);
        List<StoreResponse> hotels = filterByCategory(allSubEntities, "hotel", lat, lng);
        List<StoreResponse> industries = filterByCategory(allSubEntities, "industrie", lat, lng);
        List<StoreResponse> stores = filterByCategory(allSubEntities, "store", lat, lng);

        // 💰 3️⃣ — Deals filtrés (option prix / q)
        List<DealResponse> deals = dealRepository.findAll().stream()
                .filter(d -> q == null || d.getDescription().toLowerCase().contains(q.toLowerCase()))
                .filter(d -> d.getPrice().amount().doubleValue() >= Double.MIN_VALUE &&
                        d.getPrice().amount().doubleValue() <= Double.MAX_VALUE)
                .map(DealResponse::fromEntity)
                .limit(10)
                .collect(Collectors.toList());

        // 📦 4️⃣ — Construction de la réponse globale
        return new GlobalSearchResponse(
                stores,
                restaurants,
                boulangeries,
                hotels,
                industries,
                deals
        );
    }

    private List<StoreResponse> filterByCategory(List<SubEntity> stores, String keyword, double lat, double lng) {
        return stores.stream()
                .filter(store -> {
                    boolean matchByName = store.getName() != null &&
                            store.getName().toLowerCase().contains(keyword.toLowerCase());

                    boolean matchByDomain = store.getSubEntityDomains() != null &&
                            store.getSubEntityDomains().stream()
                                    .anyMatch(d -> d.getName() != null &&
                                            d.getName().toLowerCase().contains(keyword.toLowerCase()));

                    return matchByName || matchByDomain;
                })
                .map(store -> {
                    double distance = DistanceCalculator.calculateDistance(
                            lat, lng,
                            store.getCoordinates().latitude().doubleValue(),
                            store.getCoordinates().longitude().doubleValue()
                    );

                    List<DomainResponse> domains = store.getSubEntityDomains().stream()
                            .map(d -> new DomainResponse(d.getId(), d.getName()))
                            .collect(Collectors.toList());

                    return new StoreResponse(
                            store.getId(),
                            store.getName(),
                            store.getAvatarPath(),
                            store.getCoverPath(),
                            distance,
                            store.getNumberOfLikes(),
                            store.getNumberOfStars(),
                            domains
                    );
                })
                .limit(10)
                .collect(Collectors.toList());
    }



}

