package net.foodeals.filters.infrastructure;


import lombok.RequiredArgsConstructor;
import net.foodeals.filters.application.dtos.GlobalSearchResponse;
import net.foodeals.filters.application.services.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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


    @GetMapping("/search-data")
    public ResponseEntity<?> getSearchData(
            @RequestParam(required = false) List<String> types) {

        Map<String, Object> response = new HashMap<>();

        // Si aucun type n’est spécifié → on renvoie tout
        if (types == null || types.isEmpty()) {
            types = List.of("quantitySizes", "commerceCategories", "dateOptions",
                    "priceRange", "trendingSearches", "deliveryMethods",
                    "foodCategories", "productTypes");
        }

        for (String type : types) {
            switch (type) {
                case "quantitySizes":
                    response.put("quantitySizes", searchService.getQuantitySizes());
                    break;
                case "commerceCategories":
                    response.put("commerceCategories", searchService.getCommerceCategories());
                    break;
                case "dateOptions":
                    response.put("dateOptions", searchService.getDateOptions());
                    break;
                case "priceRange":
                    response.put("priceRange", searchService.getPriceRange());
                    break;
                case "trendingSearches":
                    response.put("trendingSearches", searchService.getTrendingSearches());
                    break;
                case "deliveryMethods":
                    response.put("deliveryMethods", searchService.getDeliveryMethods());
                    break;
                case "foodCategories":
                    response.put("foodCategories", searchService.getFoodCategories());
                    break;
                case "productTypes":
                    response.put("productTypes", searchService.getProductTypes());
                    break;
                default:
                    // on ignore les types inconnus
                    break;
            }
        }

        return ResponseEntity.ok(response);
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
