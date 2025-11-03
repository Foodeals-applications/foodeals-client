package net.foodeals.user.application.services;

import net.foodeals.core.domain.entities.SearchHistory;

import java.util.List;
import java.util.Map;


public interface SearchHistoryService {
	
	 List<SearchHistory> getUserSearchHistory(Integer userId);
	 SearchHistory saveSearch(Integer userId, String keyword);
	 void clearUserSearchHistory(Integer userId);
	 List<Map<String, Object>> getTrendingSearches();

}
