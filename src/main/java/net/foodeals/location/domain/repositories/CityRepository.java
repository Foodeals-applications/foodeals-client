package net.foodeals.location.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.location.domain.entities.City;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CityRepository extends BaseRepository<City, UUID> {
	List<City> findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(String name, Pageable pageable);

	default City findByName(String name) {
		return findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(name, PageRequest.of(0, 1))
				.stream()
				.findFirst()
				.orElse(null);
	}

	@Query("SELECT c FROM City c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<City> findByNameLikeIgnoreCase(@Param("name") String name);
}
