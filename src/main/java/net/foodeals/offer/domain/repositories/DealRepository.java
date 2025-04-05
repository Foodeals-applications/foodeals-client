package net.foodeals.offer.domain.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.enums.DealStatus;

public interface DealRepository extends BaseRepository<Deal, UUID> {

	@Query("SELECT d FROM Deal d WHERE d.offer.id = :offerId")
	public Deal getDealByOfferId(UUID offerId);

	/*
	 * @Query("SELECT d FROM Deal d " + "WHERE d.dealStatus = :status " +
	 * "AND d.offer.openTime.to < :now") Page<Deal> findExpiredDeals(DealStatus
	 * status, Instant now, Pageable pageable);
	 */

	@Query("""
			    SELECT d FROM Deal d
			    JOIN d.offer o
			    JOIN o.openTime ot
			    WHERE d.dealStatus = :unavailableStatus
			    OR (d.dealStatus = :expiredStatus AND ot.to < :today)
			""")
	Page<Deal> findExpiredAndUnavailableDeals(DealStatus unavailableStatus, DealStatus expiredStatus, String today,
			Pageable pageable);

	@Query("SELECT d FROM Deal d " + "JOIN d.offer o " + "JOIN o.orders ord "
			+ "WHERE ord.client IS NULL AND ord.clientPro IS NOT NULL")
	Page<Deal> findDealsWithProClients(Pageable pageable);

/*	@Query("SELECT SUM(d.price.amount) FROM Deal d WHERE d.subentity.id = :subentityId")
	BigDecimal calculateTotalSales(@Param("subentityId") UUID subentityId);*/

	@Query("SELECT d FROM Deal d WHERE d.product.id = :productId")
	Optional<Deal> findActiveDealByProduct(UUID productId);


}
