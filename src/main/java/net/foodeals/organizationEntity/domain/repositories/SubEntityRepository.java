package net.foodeals.organizationEntity.domain.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityStatus;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;

public interface SubEntityRepository extends JpaRepository<SubEntity, UUID> {

	@Modifying
	@Transactional
	@Query("UPDATE #{#entityName} e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = :id")
	void softDelete(UUID id);

	@Query("SELECT s FROM SubEntity s JOIN s.activities a JOIN a.offers o JOIN o.orders ord WHERE ord.id = :orderId")
	SubEntity findSubEntityByOrderId(UUID orderId);

	@Query("SELECT s FROM SubEntity s " + "LEFT JOIN s.manager m " + "LEFT JOIN s.address a "
			+ "LEFT JOIN s.activities ac " + "LEFT JOIN s.solutions sol "
			+ "WHERE  (:raisonSociale IS NULL OR s.name = :raisonSociale) "
			+ "AND (:managerId IS NULL OR m.id = :managerId) " + "AND (:email IS NULL OR m.email = :email) "
			+ "AND (:phone IS NULL OR m.phone = :phone) " + "AND (:cityId IS NULL OR a.city.id = :cityId) "
			+ "AND (:solutionId IS NULL OR sol.id = :solutionId)"
			+ "AND (coalesce(:startDate, null) IS NULL OR s.createdAt >= :startDate)"
		    + "AND (coalesce(:endDate, null) IS NULL OR s.createdAt >= :endDate)"
			+ "")
	Page<SubEntity> filterSubEntities(@Param("raisonSociale") String raisonSociale, @Param("managerId") UUID managerId,
			@Param("email") String email, @Param("phone") String phone, @Param("cityId") UUID cityId,
			@Param("solutionId") UUID solutionId,
			@Param("startDate") Instant startDate,
	        @Param("endDate") Instant endDate,
			Pageable pageable);
	
	
	Page<SubEntity> findAllBySubEntityStatus(SubEntityStatus status, Pageable pageable);
	
	List<SubEntity> findByType(SubEntityType type);
	
    @Query("SELECT s FROM SubEntity s JOIN s.activities a WHERE a.name = :activityName")
    List<SubEntity> findByActivityName(@Param("activityName") String activityName);
    
    @Query("SELECT a.name, COUNT(s) FROM SubEntity s JOIN s.activities a GROUP BY a.name")
    List<Object[]> countStoresByActivity();

}
