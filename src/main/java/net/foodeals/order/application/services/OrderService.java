package net.foodeals.order.application.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.zxing.WriterException;
import net.foodeals.order.application.dtos.requests.CreateOrderRequest;
import net.foodeals.order.application.dtos.responses.CreateOrderResponse;
import net.foodeals.order.application.dtos.responses.OrderConfirmationResponse;
import net.foodeals.order.application.dtos.responses.OrderDetailsResponse;
import net.foodeals.order.application.dtos.responses.OrderResponse;
import net.foodeals.user.domain.entities.User;
import org.springframework.web.multipart.MultipartFile;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.order.application.dtos.requests.OrderRequest;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.enums.OrderSource;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;

public interface OrderService extends CrudService<Order, UUID, OrderRequest> {

	public Map<String, List<OrderResponse>> findOrdersByClient(User client);

	public OrderDetailsResponse getDetailsOrder(UUID id)  throws WriterException;

   public OrderConfirmationResponse getOrderConfirmation(UUID orderId);

    public CreateOrderResponse createOrder(User user, CreateOrderRequest request);

}
