package net.foodeals.user.infrastructure.interfaces.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.user.application.services.SearchHistoryService;
import net.foodeals.user.domain.entities.SearchHistory;

@RestController
@RequestMapping("/v1/search-history")
@RequiredArgsConstructor
public class SearchHistoryController {

	private final SearchHistoryService service;

	@GetMapping("/{userId}")
	public ResponseEntity<List<SearchHistory>> getUserSearchHistory(@PathVariable Integer userId) {
		return ResponseEntity.ok(service.getUserSearchHistory(userId));
	}

	@PostMapping("/add")
	public ResponseEntity<SearchHistory> addSearch(@RequestBody Map<String, String> request) {
		Integer userId = Integer.valueOf(request.get("userId"));
		String keyword = request.get("keyword");
		return ResponseEntity.ok(service.saveSearch(userId, keyword));
	}

	@DeleteMapping("/{userId}/clear")
	public ResponseEntity<Void> clearUserHistory(@PathVariable Integer userId) {
		service.clearUserSearchHistory(userId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/trending/user/{userId}")
	public ResponseEntity<List<Map<String, Object>>> getTrendingSearches(@PathVariable Integer userId) {
		return ResponseEntity.ok(service.getTrendingSearchesForUser(userId));
	}
}
