package net.foodeals.delivery.application.services.impl;

import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.delivery.domain.entities.CoveredZones;
import net.foodeals.organizationEntity.application.dtos.requests.CoveredZonesDto;


public interface  CoveredZonesService  extends CrudService<CoveredZones, UUID, CoveredZonesDto>{

    
}
