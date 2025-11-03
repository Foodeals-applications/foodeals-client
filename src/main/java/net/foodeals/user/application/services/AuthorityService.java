package net.foodeals.user.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.Authority;
import net.foodeals.user.application.dtos.requests.AuthorityRequest;

import java.util.UUID;

public interface AuthorityService extends CrudService<Authority, UUID, AuthorityRequest> {

}
