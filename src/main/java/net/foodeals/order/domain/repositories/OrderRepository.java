package net.foodeals.order.domain.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.enums.OrderSource;
import net.foodeals.order.domain.enums.OrderStatus;

public interface OrderRepository extends BaseRepository<Order, UUID> {

	@Query("SELECT o FROM Order o WHERE o.createdAt >= :startOfDay AND o.createdAt < :endOfDay AND o.status = :status")
	List<Order> findOrdersByDateRangeAndStatus(Instant startOfDay, Instant endOfDay, OrderStatus status);
	
	
	@Query("SELECT o FROM Order o WHERE  o.createdAt < :today AND o.status = :status")
	List<Order> findHistoryOrdersByStatus(Instant today, OrderStatus status);

	@Query("SELECT o FROM Order o WHERE o.createdAt >= :startOfDay AND o.createdAt < :endOfDay")
	List<Order> findOrdersByDateRange(Instant startOfDay, Instant endOfDay);

	@Query("SELECT o FROM Order o WHERE o.delivery.id IS NULL")
	List<Order> findOrdersNotAffected();
	
	@Query("SELECT o FROM Order o WHERE o.delivery.id IS NOT NULL")
	List<Order> findOrdersAffected();
	
	@Query("SELECT o FROM Order o WHERE o.delivery.id IS NULL AND o.orderSource = :orderSource")
	List<Order> findOrdersNotAffectedBySource(@Param("orderSource") OrderSource orderSource);
	
	
	@Query("SELECT o FROM Order o WHERE o.clientPro.id = :subEntityId AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findBySubEntityAndDateRange(UUID subEntityId, Instant startDate, Instant endDate);
	
	@Query("SELECT o FROM Order o WHERE o.clientPro.id = :clientId")
	List<Order> findByClientPro(UUID clientId);
	
	
	
	
	
	
	
}
