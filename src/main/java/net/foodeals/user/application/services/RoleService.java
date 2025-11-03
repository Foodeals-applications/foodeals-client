package net.foodeals.user.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.Role;
import net.foodeals.user.application.dtos.requests.RoleRequest;

import java.util.List;
import java.util.UUID;

public interface RoleService extends CrudService<Role, UUID, RoleRequest> {
    Role findByName(String name);
    
    List<Role> findAllExcludingAdminAndSuperAdminAndClient();
}
