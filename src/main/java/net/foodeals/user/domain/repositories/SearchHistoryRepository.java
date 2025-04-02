package net.foodeals.user.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.user.domain.entities.SearchHistory;

public interface SearchHistoryRepository extends BaseRepository<SearchHistory, UUID> {

	List<SearchHistory> findByUserIdOrderBySearchedAtDesc(Integer userId);

	@Query("SELECT sh.keyword, COUNT(sh.keyword) AS count FROM SearchHistory sh " +
		       "GROUP BY sh.keyword ORDER BY count DESC")
		List<Object[]> findTrendingSearches();

}
