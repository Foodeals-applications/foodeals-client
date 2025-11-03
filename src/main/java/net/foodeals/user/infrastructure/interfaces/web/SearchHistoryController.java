package net.foodeals.user.infrastructure.interfaces.web;

import java.util.List;
import java.util.Map;

import net.foodeals.core.domain.entities.SearchHistory;
import net.foodeals.core.domain.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.user.application.services.SearchHistoryService;
import net.foodeals.user.application.services.UserService;

@RestController
@RequestMapping("/v1/search-history")
@RequiredArgsConstructor
public class SearchHistoryController {

	private final SearchHistoryService service;
	private final UserService userService ;

	@GetMapping
	public ResponseEntity<List<SearchHistory>> getUserSearchHistory() {
		User client =userService.getConnectedUser();
		Integer userId = client.getId();
		return ResponseEntity.ok(service.getUserSearchHistory(userId));
	}

	@PostMapping("/add")
	public ResponseEntity<SearchHistory> addSearch(@RequestBody Map<String, String> request) {
		User client =userService.getConnectedUser();
		Integer userId = client.getId();
		String keyword = request.get("keyword");
		return ResponseEntity.ok(service.saveSearch(userId, keyword));
	}

	@DeleteMapping("/clear")
	public ResponseEntity<Void> clearUserHistory() {
		User client =userService.getConnectedUser();
		Integer userId = client.getId();
		service.clearUserSearchHistory(userId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/trending")
	public ResponseEntity<List<Map<String, Object>>> getTrendingSearches() {
		return ResponseEntity.ok(service.getTrendingSearches());
	}
}
