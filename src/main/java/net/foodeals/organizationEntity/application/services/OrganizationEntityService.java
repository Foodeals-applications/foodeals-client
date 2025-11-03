package net.foodeals.organizationEntity.application.services;

import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.application.dtos.requests.OrganizationEntityRequest;

public interface OrganizationEntityService extends CrudService<OrganizationEntity,UUID,OrganizationEntityRequest> {

}
