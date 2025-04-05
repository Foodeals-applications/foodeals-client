package net.foodeals.user.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.user.domain.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.role.name = :roleName")
	Page<User> findByRoleName(@Param("roleName") String roleName, Pageable pageable);

	@Query("SELECT u FROM User u WHERE LOWER(u.role.name) IN ('manager', 'sales_manager', 'manager_regionale')")
	List<User> findManagersAndSalesManagers();

	@Query("SELECT u FROM User u JOIN FETCH u.role r JOIN FETCH r.authorities WHERE u.email = :email")
	Optional<User> findByEmailWithRoleAndAuthorities(@Param("email") String email);

	@Query("SELECT COUNT(u) FROM User u WHERE u.organizationEntity.id = :organizationId AND u.role.name = 'DELIVERY_MAN'")
	Long countDeliveryUsersByOrganizationId(@Param("organizationId") UUID organizationId);
	
	
	@Query("SELECT u FROM User u WHERE u.organizationEntity.id = :organizationId AND u.role.name = 'DELIVERY_MAN'")
	Page<User> findDeliveryUsersByOrganizationId(@Param("organizationId") UUID organizationId,Pageable pageable);

	Page<User> findByOrganizationEntity_Id(UUID organizationId, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.organizationEntity.id = :organizationId AND u.role.name = 'MANAGER'")
	User findManagerOfOrganizationEntity(UUID organizationId);

	@Query("SELECT u FROM User u WHERE LOWER(u.name.lastName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.name.firstName) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<User> findByLastNameOrFirstName(@Param("name") String name);

	@Query("""
			SELECT u FROM User u
			LEFT JOIN u.role r
			WHERE (:name IS NULL
			        OR u.name.firstName LIKE CONCAT('%', :name, '%')
			        OR u.name.lastName LIKE CONCAT('%', :name, '%'))
			  AND (:role IS NULL OR r.name = :role)
			""")
	Page<User> findByNameAndRole(@Param("name") String name, @Param("role") String role, Pageable pageable);

	@Query("""
			    SELECT u
			    FROM User u
			    WHERE LOWER(u.role.name) IN ('manager', 'sales_manager', 'manager_regionale')
			    AND u.id NOT IN (
			        SELECT s.manager.id
			        FROM SubEntity s
			        WHERE s.manager IS NOT NULL
			    )
			""")
	List<User> findAvailableManagers();

	@Query("""
			    SELECT u FROM User u
			    WHERE (coalesce(:startDate, null) IS NULL OR u.createdAt >= :startDate)
			    AND (coalesce(:endDate, null) IS NULL OR u.createdAt >= :endDate)
			    AND (:collaboratorId IS NULL OR u.id = :collaboratorId)
			    AND (:role IS NULL OR u.role.name = :role)
			    AND (:email IS NULL OR u.email LIKE %:email%)
			    AND (:phone IS NULL OR u.phone LIKE %:phone%)
			    AND (:solutionId IS NULL OR :solutionId MEMBER OF u.solutions)
			""")
	Page<User> filterUsers(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate,
			@Param("collaboratorId") Integer collaboratorId, @Param("role") String role, @Param("email") String email,
			@Param("phone") String phone, @Param("solutionId") UUID solutionId, Pageable pageable);

}
