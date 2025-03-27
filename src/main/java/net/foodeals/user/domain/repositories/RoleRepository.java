package net.foodeals.user.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.user.domain.entities.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends BaseRepository<Role, UUID> {
    Optional<Role> findByName(String name);
    
    
    @Query("SELECT r FROM Role r WHERE r.name NOT IN ('ADMIN', 'SUPER_ADMIN','CLIENT')")
    List<Role> findAllExcludingAdminAndSuperAdminAndClient();
}
