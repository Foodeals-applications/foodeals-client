package net.foodeals.dlc.application.services;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.dlc.application.dtos.requests.ModificationRequest;
import net.foodeals.dlc.application.dtos.responses.ModificationDetailsResponse;
import net.foodeals.dlc.domain.entities.Modification;

public interface ModificationService extends CrudService<Modification, UUID, ModificationRequest>  {
	
	List<Modification> getModificationsByDlc(UUID dlcId);
	ModificationDetailsResponse getUserModificationDetails(UUID dlcId, Integer userId);
	 
	

}
