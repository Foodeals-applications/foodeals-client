package net.foodeals.filters.infrastructure;


import lombok.RequiredArgsConstructor;
import net.foodeals.filters.application.dtos.GlobalSearchResponse;
import net.foodeals.filters.application.services.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/filters")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/suggestions")
    public ResponseEntity<?> getSuggestions(@RequestParam String q) {
        return ResponseEntity.ok(searchService.getSuggestions(q));
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentSearches() {
        return ResponseEntity.ok(searchService.getRecentSearches());
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveSearch(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(searchService.saveSearch(body));
    }

    @DeleteMapping("/recent/{query}")
    public ResponseEntity<?> deleteRecent(@PathVariable String query) {
        return ResponseEntity.ok(searchService.deleteRecent(query));
    }

    @GetMapping("/quantity-sizes")
    public ResponseEntity<?> getQuantitySizes() {
        return ResponseEntity.ok(searchService.getQuantitySizes());
    }

    @GetMapping("/commerce-categories")
    public ResponseEntity<?> getCommerceCategories() {
        return ResponseEntity.ok(searchService.getCommerceCategories());
    }

    @GetMapping("/date-options")
    public ResponseEntity<?> getDateOptions() {
        return ResponseEntity.ok(searchService.getDateOptions());
    }

    @GetMapping("/price-range")
    public ResponseEntity<?> getPriceRange() {
        return ResponseEntity.ok(searchService.getPriceRange());
    }

    @GetMapping("/trending")
    public ResponseEntity<?> getTrendingSearches() {
        return ResponseEntity.ok(searchService.getTrendingSearches());
    }



    @GetMapping("/delivery-methods")
    public ResponseEntity<?> getDeliveryMethods() {
        return ResponseEntity.ok(searchService.getDeliveryMethods());
    }

    @GetMapping("/food-categories")
    public ResponseEntity<?> getFoodCategories() {
        return ResponseEntity.ok(searchService.getFoodCategories());
    }

    @GetMapping("/product-types")
    public ResponseEntity<?> getProductTypes() {
        return ResponseEntity.ok(searchService.getProductTypes());
    }

    @GetMapping("/global")
    public ResponseEntity<GlobalSearchResponse> globalSearch(
            @RequestParam String q,
            @RequestParam(defaultValue = "all") String type,
            @RequestParam String location
    ) {
        String[] loc = location.split(",");
        double lat = Double.parseDouble(loc[0]);
        double lng = Double.parseDouble(loc[1]);

        GlobalSearchResponse response = searchService.globalSearch(q, type, lat, lng);
        return ResponseEntity.ok(response);
    }
}
