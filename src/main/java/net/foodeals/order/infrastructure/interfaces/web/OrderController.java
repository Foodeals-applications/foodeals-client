package net.foodeals.order.infrastructure.interfaces.web;

import lombok.RequiredArgsConstructor;
import net.foodeals.order.application.dtos.requests.CreateOrderRequest;
import net.foodeals.order.application.dtos.responses.*;
import net.foodeals.order.application.services.DeliveryTrackingService;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final DeliveryTrackingService deliveryTrackingService;
    private final UserService userService;

    @GetMapping("/orders-client")
    public ResponseEntity<Map<String, List<OrderResponse>>> getOrdersByClient() {
        User client = userService.getConnectedUser();
        final Map<String, List<OrderResponse>> response = service.findOrdersByClient(client);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details-order/{id}")
    public ResponseEntity<OrderDetailsResponse> getDetailsOrders(@PathVariable UUID id) {

        final OrderDetailsResponse  response = service.getDetailsOrder(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/delivery/{id}/tracking")
    public ResponseEntity<DeliveryTrackingResponse> getTracking(@PathVariable UUID id) {
        DeliveryTrackingResponse response = deliveryTrackingService.getTracking(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/confirmation")
    public ResponseEntity<OrderConfirmationResponse> getOrderConfirmation(@PathVariable UUID id) {
        OrderConfirmationResponse response = service.getOrderConfirmation(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateOrderResponse> createOrder(
            @RequestBody CreateOrderRequest request) {
        User user=userService.getConnectedUser();
        return ResponseEntity.ok(service.createOrder(user, request));
    }

}
