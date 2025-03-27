package net.foodeals.offer.domain.repositories;

import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.RelaunchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RelaunchHistoryRepository extends JpaRepository<RelaunchHistory, UUID> {
	
	
	 List<RelaunchHistory> findByBox(Box box);
    
}