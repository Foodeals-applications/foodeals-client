package net.foodeals.order.application.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.delivery.application.services.DeliveryService;
import net.foodeals.location.application.services.AddressService;
import net.foodeals.offer.application.services.OfferService;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.order.application.dtos.requests.OrderRequest;
import net.foodeals.order.application.services.CouponService;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.enums.OrderSource;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.enums.OrderType;
import net.foodeals.order.domain.exceptions.OrderNotFoundException;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository repository;

	private final CouponService couponService;
	private final AddressService addressService;
	private final UserService userService;
	private final OfferService offerService;
	private final DeliveryService deliveryService;

	@Value("${upload.directory}")
	private String uploadDir;

	@Override
	public List<Order> findAll() {
		return repository.findAll();
	}

	@Override
	public Page<Order> findAll(Integer pageNumber, Integer pageSize) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public Order findById(UUID id) {
		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
		if (!order.isSeen()) {
			order.setSeen(true);
		}
		return order;
	}

	@Override
	public Order create(OrderRequest request) {
		final User client = userService.findById(request.clientId());
		final Offer offer = offerService.findById(request.offerId());

		final Order order = Order.create(request.type(), request.status(), client, offer);

		if (request.type().equals(OrderType.DELIVERY)) {
			order.setShippingAddress(addressService.create(request.shippingAddress()))
					.setDelivery(deliveryService.create(request.delivery()));
		}

		if (request.couponId() != null) {
			order.setCoupon(couponService.findById(request.couponId()));
		}

		return repository.save(order);
	}

	@Override
	public Order update(UUID id, OrderRequest request) {
		return null;
	}

	@Override
	public void delete(UUID id) {
		if (repository.existsById(id))
			throw new OrderNotFoundException(id);

		repository.softDelete(id);
	}

	@Transactional
	@Override
	public List<Order> getOrdersForTodayAndStatus(String status) {
		LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
		LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

		Instant startOfDayInstant = startOfDay.atZone(ZoneId.systemDefault()).toInstant();
		Instant endOfDayInstant = endOfDay.atZone(ZoneId.systemDefault()).toInstant();
		OrderStatus orderStatus = OrderStatus.valueOf(status);
		return repository.findOrdersByDateRangeAndStatus(startOfDayInstant, endOfDayInstant, orderStatus);
	}

	@Override
	public List<Order> getOrdersForToday() {
		LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
		LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

		Instant startOfDayInstant = startOfDay.atZone(ZoneId.systemDefault()).toInstant();
		Instant endOfDayInstant = endOfDay.atZone(ZoneId.systemDefault()).toInstant();
		return repository.findOrdersByDateRange(startOfDayInstant, endOfDayInstant);
	}

	@Override
	public Order cancelOrder(UUID id, String reason, String subject, MultipartFile attachment) throws Exception {
		Order order = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));

		if (order.getStatus() == OrderStatus.CANCELED) {
			throw new Exception("Order is already canceled");
		}

		order.setStatus(OrderStatus.CANCELED);
		order.setCancellationReason(reason);
		order.setCancellationDate(LocalDateTime.now());

		order.setCancellationSubject(subject);
		if (attachment != null && !attachment.isEmpty()) {

			String attachmentPath = null;
			try {
				attachmentPath = saveAttachment(attachment);
			} catch (IOException e) {
				e.printStackTrace();
			}
			order.setAttachmentPath(attachmentPath);
		}

		return repository.save(order);
	}

	private String saveAttachment(MultipartFile file) throws IOException {
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(uploadDir, fileName);

		try {
			Files.createDirectories(filePath.getParent());
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("Failed to save attachment");
		}

		return filePath.toString();
	}

	@Override
	public List<Order> getAllOrdersNotAffected() {
		return repository.findOrdersNotAffected();
	}

	@Override
	public List<Order> getAllOrdersNotAffectedBySource(OrderSource orderSource) {
		return repository.findOrdersNotAffectedBySource(orderSource);
	}

	@Override
	public List<Order> getOrdersForOrganization(OrganizationEntity organizationEntity) {

		List<SubEntity> subEntities = organizationEntity.getSubEntities();

		List<Order> allOrders = new ArrayList<>();

		for (SubEntity subEntity : subEntities) {
			List<Activity> activities = subEntity.getActivities();
			for (Activity activity : activities) {
				List<Offer> offers = activity.getOffers();

				for (Offer offer : offers) {
					List<Order> orders = offer.getOrders();
					for (Order order : orders) {
						if (order.getClient() != null && order.getClientPro() == null)
							allOrders.add(order);
					}

				}
			}
		}
		return allOrders;
	}

	@Override
	public List<Order> getOrdersDealProForOrganization(OrganizationEntity organizationEntity, OrderStatus orderStatus) {

		List<SubEntity> subEntities = organizationEntity.getSubEntities();

		List<Order> allOrders = new ArrayList<>();

		for (SubEntity subEntity : subEntities) {
			List<Activity> activities = subEntity.getActivities();
			for (Activity activity : activities) {
				List<Offer> offers = activity.getOffers();

				for (Offer offer : offers) {
					List<Order> orders = offer.getOrders();
					for (Order order : orders) {
						if (order.getClient() == null && order.getClientPro() != null
								&& order.getStatus().compareTo(orderStatus) == 0)
							allOrders.add(order);
					}

				}
			}
		}
		return allOrders;
	}

	@Override
	public List<Order> getOrdersHistoryAndStatus(String status) {
		LocalDateTime todayLocalTime = LocalDateTime.now();
	

		Instant today = todayLocalTime.atZone(ZoneId.systemDefault()).toInstant();
		OrderStatus orderStatus = OrderStatus.valueOf(status);
		return repository.findHistoryOrdersByStatus(today, orderStatus);
	}

	@Override
	public List<Order> getAllOrdersAffected() {
		return repository.findOrdersAffected();
	}
}
