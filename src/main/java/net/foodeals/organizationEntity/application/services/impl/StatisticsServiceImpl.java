package net.foodeals.organizationEntity.application.services.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.enums.OrderType;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.organizationEntity.application.dtos.responses.StatisticsResponse;
import net.foodeals.organizationEntity.application.services.StatisticsService;
import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

	private final OrderService orderService;

	public StatisticsResponse getStatistics(OrganizationEntity organizationEntity, LocalDate startDate,
			LocalDate endDate) {

		Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
		Instant endInstant = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

		List<SubEntity> subEntities = organizationEntity.getSubEntities();
		List<Order> allOrders = new ArrayList<>();

		for (SubEntity subEntity : subEntities) {
			List<Activity> activities = subEntity.getActivities();
			for (Activity activity : activities) {
				List<Offer> offers = activity.getOffers();
				for (Offer offer : offers) {
					List<Order> orders = offer.getOrders();

					for (Order order : orders) {
						if (order.getClient() != null && order.getClientPro() == null) {
							Instant orderCreatedAt = order.getCreatedAt(); //
							if (orderCreatedAt.isAfter(startInstant) && orderCreatedAt.isBefore(endInstant)) {
								allOrders.add(order);
							}
						}
					}
				}
			}
		}

		BigDecimal totalWithDelivery = allOrders.stream().filter(order -> order.getDelivery() != null)
				.map(order -> order.getOffer().getSalePrice().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalWithoutDelivery = allOrders.stream().filter(order -> order.getDelivery() == null)
				.map(order -> order.getOffer().getSalePrice().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);

		long deliveredCount = allOrders.stream().filter(order -> order.getStatus() == OrderStatus.COMPLETED).count();
		long notDeliveredCount = allOrders.stream().filter(order -> order.getStatus() == OrderStatus.IN_PROGRESS)
				.count();
		long dineInCount = allOrders.stream().filter(order -> order.getType() == OrderType.AT_PLACE).count();
		long takeAwayCount = allOrders.stream().filter(order -> order.getType() == OrderType.PICKUP).count();
		long deliveryCount = allOrders.stream().filter(order -> order.getType() == OrderType.DELIVERY).count();
		long canceledCount = allOrders.stream().filter(order -> order.getStatus() == OrderStatus.CANCELED).count();
		long archivedCount = allOrders.stream().filter(Order::isSeen).count();

		return new StatisticsResponse(totalWithDelivery, totalWithoutDelivery, deliveredCount, notDeliveredCount,
				dineInCount, takeAwayCount, deliveryCount, canceledCount, archivedCount);
	}
}
