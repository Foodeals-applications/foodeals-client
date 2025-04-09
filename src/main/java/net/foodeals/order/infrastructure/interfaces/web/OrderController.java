package net.foodeals.order.infrastructure.interfaces.web;

import java.util.List;
import java.util.Map;

import net.foodeals.user.domain.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import net.foodeals.order.application.dtos.responses.OrderResponse;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.user.application.services.UserService;


@RestController
@RequestMapping("v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final UserService userService;

    @GetMapping("/orders-client")
    public ResponseEntity<Map<String, List<OrderResponse>>> getOrdersByClient() {
        User client = userService.getConnectedUser();
        final Map<String, List<OrderResponse>> response = service.findOrdersByClient(client);
        return ResponseEntity.ok(response);
    }


}
