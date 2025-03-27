package net.foodeals.dlc.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.dlc.domain.entities.Modification;

public interface ModificationRepository extends BaseRepository<Modification, UUID> {

	 List<Modification> findByDlcId(UUID dlcId);
	 @Query("SELECT m FROM Modification m WHERE m.dlc.id = :dlcId AND m.user.id = :userId ORDER BY m.modificationDate DESC")
	 List<Modification>  findLastModificationByDlcIdAndUserId(UUID dlcId, Integer userId);
}
