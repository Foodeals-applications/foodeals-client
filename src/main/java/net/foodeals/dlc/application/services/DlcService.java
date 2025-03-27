package net.foodeals.dlc.application.services;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.dlc.application.dtos.requests.DlcRequest;
import net.foodeals.dlc.application.dtos.responses.DlcDetailsResponse;
import net.foodeals.dlc.domain.entities.Dlc;

public interface DlcService extends CrudService<Dlc, UUID, DlcRequest> {

	DlcDetailsResponse findDlcDetails(UUID id);
	Dlc  applyDiscount(UUID dlcId, int discountPercentage);
	
	

}
