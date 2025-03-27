package net.foodeals.offer.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.ModifiyHistory;

public interface ModifiyHistoryRepository extends JpaRepository<ModifiyHistory, UUID> {
	
	 List<ModifiyHistory> findByBox(Box box);
    
}