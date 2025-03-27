package net.foodeals.delivery.domain.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.delivery.domain.entities.Delivery;

public interface DeliveryRepository extends BaseRepository<Delivery, UUID> {
	@Query("SELECT COUNT(d) FROM Delivery d WHERE d.deliveryBoy.organizationEntity.id = :organizationEntityId AND d.status = DELIVERED")
	Long countDeliveriesByDeliveryPartner(@Param("organizationEntityId") UUID organizationEntityId);

	@Query("SELECT COUNT(d) FROM Delivery d WHERE d.deliveryBoy.organizationEntity.id = :organizationId")
	Long countDeliveriesByOrganization(@Param("organizationId") UUID organizationId);

	@Query("""
			    SELECT COUNT(d)
			    FROM Delivery d
			    WHERE d.deliveryBoy.id = :deliveryBoyId
			    AND d.status = 'ON_THE_WAY'
			    AND d.createdAt BETWEEN :startDate AND :endDate
			""")
	Long countInProgressDeliveriesByDeliveryBoy(@Param("deliveryBoyId") Integer deliveryBoyId,
			@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

	@Query("""
			    SELECT COUNT(d)
			    FROM Delivery d
			    WHERE d.deliveryBoy.id = :deliveryBoyId
			    AND d.status = 'PENDING'
			    AND d.createdAt BETWEEN :startDate AND :endDate
			""")
	Long countScheduledDeliveriesByDeliveryBoy(@Param("deliveryBoyId") Integer deliveryBoyId,
			@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

	@Query("""
			    SELECT COUNT(d)
			    FROM Delivery d
			    WHERE d.deliveryBoy.id = :deliveryBoyId
			    AND d.status = 'DELIVERED'
			    AND d.createdAt BETWEEN :startDate AND :endDate
			""")
	Long countDeliveredDeliveriesByDeliveryBoy(@Param("deliveryBoyId") Integer deliveryBoyId,
			@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

	@Query("""
			    SELECT COUNT(d)
			    FROM Delivery d
			    WHERE d.deliveryBoy.id = :deliveryBoyId
			    AND d.createdAt BETWEEN :startDate AND :endDate
			""")
	Long countTotalDeliveriesByDeliveryBoy(@Param("deliveryBoyId") Integer deliveryBoyId,
			@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

	@Query("SELECT COUNT(u) FROM User u WHERE u.role.name = 'DELIVERY_MAN' AND u.status = 'ACTIVE' AND u.organizationEntity.id = :organizationId")
	long countActiveDeliveryBoys(@Param("organizationId") UUID organizationId);

	@Query("SELECT COUNT(u) FROM User u WHERE u.role.name = 'DELIVERY_MAN' AND u.status = 'INACTIVE' AND u.organizationEntity.id = :organizationId")
	long countInactiveDeliveryBoys(@Param("organizationId") UUID organizationId);
}
