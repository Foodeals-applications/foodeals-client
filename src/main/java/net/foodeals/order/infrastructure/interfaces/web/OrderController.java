package net.foodeals.order.infrastructure.interfaces.web;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.order.application.dtos.requests.CancelRequest;
import net.foodeals.order.application.dtos.requests.OrderRequest;
import net.foodeals.order.application.dtos.responses.OrderDetailsResponse;
import net.foodeals.order.application.dtos.responses.OrderResponse;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.enums.OrderSource;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

@RestController
@RequestMapping("v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService service;
	private final UserService userService;
	private final ModelMapper mapper;

	@GetMapping
	public ResponseEntity<Page<OrderResponse>> getAll(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {

		User connectedUser = userService.getConnectedUser();
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		List<Order> orders = service.getOrdersForOrganization(connectedUser.getOrganizationEntity());
		Page<OrderResponse> orderPage = new PageImpl<>(
				orders.stream().map(order -> mapper.map(order, OrderResponse.class)).collect(Collectors.toList()),
				pageable, orders.size());
		return ResponseEntity.ok(orderPage);
	}

	@GetMapping("/dealpro/{status}")
	public ResponseEntity<Page<OrderResponse>> getAllOrdersDealPro(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize, @PathVariable String status) {

		OrderStatus orderStatus = OrderStatus.valueOf(status);
		User connectedUser = userService.getConnectedUser();
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		List<Order> orders = service.getOrdersDealProForOrganization(connectedUser.getOrganizationEntity(), orderStatus);
		Page<OrderResponse> orderPage = new PageImpl<>(
				orders.stream().map(order -> mapper.map(order, OrderResponse.class)).collect(Collectors.toList()),
				pageable, orders.size());
		return ResponseEntity.ok(orderPage);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDetailsResponse> getById(@PathVariable UUID id) {
		final OrderDetailsResponse response = mapper.map(service.findById(id), OrderDetailsResponse.class);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<OrderResponse> create(@RequestBody @Valid OrderRequest request) {
		final OrderResponse response = mapper.map(service.create(request), OrderResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<OrderResponse> update(@PathVariable UUID id, @RequestBody @Valid OrderRequest request) {
		final OrderResponse response = mapper.map(service.update(id, request), OrderResponse.class);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/today/{status}")
	public ResponseEntity<List<OrderResponse>> getTodayOrdersByStatus(@PathVariable String status) {
		List<OrderResponse> responses = service.getOrdersForTodayAndStatus(status).stream()
				.map(order -> mapper.map(order, OrderResponse.class)).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/history/{status}")
	public ResponseEntity<List<OrderResponse>> getHistoryOrdersByStatus(@PathVariable String status) {
		List<OrderResponse> responses = service.getOrdersHistoryAndStatus(status).stream()
				.map(order -> mapper.map(order, OrderResponse.class)).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/today")
	public ResponseEntity<List<OrderResponse>> getTodayOrders() {
		List<OrderResponse> responses = service.getOrdersForToday().stream()
				.map(order -> mapper.map(order, OrderResponse.class)).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/not-affected")
	public ResponseEntity<List<OrderResponse>> getAllOrdersNotAffected() {
		List<OrderResponse> responses = service.getAllOrdersNotAffected().stream()
				.map(order -> mapper.map(order, OrderResponse.class)).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/affected")
	public ResponseEntity<List<OrderResponse>> getAllOrdersAffected() {
		List<OrderResponse> responses = service.getAllOrdersAffected().stream()
				.map(order -> mapper.map(order, OrderResponse.class)).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/not-affected/{source}")
	public ResponseEntity<List<OrderResponse>> getAllOrdersNotAffectedBySource(@PathVariable String source) {
		OrderSource orderSource = OrderSource.valueOf(source);
		List<OrderResponse> responses = service.getAllOrdersNotAffectedBySource(orderSource).stream()
				.map(order -> mapper.map(order, OrderResponse.class)).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}

	@PatchMapping("/{id}/cancel")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable UUID id, @RequestPart CancelRequest cancelRequest,
			@RequestPart(value = "attachment", required = false) MultipartFile attachment) {

		try {
			return ResponseEntity
					.ok(mapper.map(service.cancelOrder(id, cancelRequest.reason(), cancelRequest.subject(), attachment),
							OrderResponse.class));
		} catch (Exception e) {

			return null;
		}

	}

	/*
	 * @GetMapping("/statistics") public ResponseEntity<Map<String, Object>>
	 * getStatistics(
	 * 
	 * @RequestParam UUID subAccountId,
	 * 
	 * @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant
	 * startDate,
	 * 
	 * @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant endDate)
	 * { Map<String, Object> stats = service.getStatistics(subAccountId, startDate,
	 * endDate); return ResponseEntity.ok(stats); }
	 */

}
