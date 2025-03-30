package net.foodeals.user.application.services;

import java.util.List;
import java.util.Map;

import net.foodeals.user.domain.entities.SearchHistory;

public interface SearchHistoryService {
	
	 List<SearchHistory> getUserSearchHistory(Integer userId);
	 SearchHistory saveSearch(Integer userId, String keyword);
	 void clearUserSearchHistory(Integer userId);
	 List<Map<String, Object>> getTrendingSearchesForUser(Integer userId);

}
