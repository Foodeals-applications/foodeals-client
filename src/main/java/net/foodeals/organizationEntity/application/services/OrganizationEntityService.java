package net.foodeals.organizationEntity.application.services;

import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.organizationEntity.application.dtos.requests.OrganizationEntityRequest;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;

public interface OrganizationEntityService extends CrudService<OrganizationEntity,UUID,OrganizationEntityRequest> {

}
