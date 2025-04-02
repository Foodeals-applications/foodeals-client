package net.foodeals.user.application.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.foodeals.user.application.services.SearchHistoryService;
import net.foodeals.user.domain.entities.SearchHistory;
import net.foodeals.user.domain.repositories.SearchHistoryRepository;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {

	private final SearchHistoryRepository repository;

	@Override
	public List<SearchHistory> getUserSearchHistory(Integer userId) {
		return repository.findByUserIdOrderBySearchedAtDesc(userId);
	}

	@Override
	public SearchHistory saveSearch(Integer userId, String keyword) {
		SearchHistory search = new SearchHistory();
		search.setUserId(userId);
		search.setKeyword(keyword);
		return repository.save(search);
	}

	@Override
	public void clearUserSearchHistory(Integer userId) {
		List<SearchHistory> searches = repository.findByUserIdOrderBySearchedAtDesc(userId);
		repository.deleteAll(searches);
	}

	@Override
	public List<Map<String, Object>> getTrendingSearches() {
	    return repository.findTrendingSearches().stream()
	            .map(result -> Map.of("keyword", result[0], "count", result[1]))
	            .collect(Collectors.toList());
	}

}
