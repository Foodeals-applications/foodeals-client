package net.foodeals.dlc.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.dlc.domain.entities.Dlc;
import net.foodeals.dlc.domain.enums.ValorisationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DlcRepository extends BaseRepository<Dlc, UUID> {
	
	 Page<Dlc> findByValorisationType(ValorisationType valorisationType, Pageable pageable);

}
