package net.foodeals.order.application.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.zxing.WriterException;
import net.foodeals.core.domain.entities.Order;
import net.foodeals.core.domain.entities.User;
import net.foodeals.order.application.dtos.requests.CreateOrderRequest;
import net.foodeals.order.application.dtos.responses.CreateOrderResponse;
import net.foodeals.order.application.dtos.responses.OrderConfirmationResponse;
import net.foodeals.order.application.dtos.responses.OrderDetailsResponse;
import net.foodeals.order.application.dtos.responses.OrderResponse;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.order.application.dtos.requests.OrderRequest;



public interface OrderService extends CrudService<Order, UUID, OrderRequest> {

	public Map<String, List<OrderResponse>> findOrdersByClient(User client);

	public OrderDetailsResponse getDetailsOrder(UUID id)  throws WriterException;

   public OrderConfirmationResponse getOrderConfirmation(UUID orderId);

    public CreateOrderResponse createOrder(User user, CreateOrderRequest request);

}
