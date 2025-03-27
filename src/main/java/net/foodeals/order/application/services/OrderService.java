package net.foodeals.order.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.order.application.dtos.requests.OrderRequest;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.enums.OrderSource;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;

public interface OrderService extends CrudService<Order, UUID, OrderRequest> {

	public List<Order> getOrdersForTodayAndStatus(String status);
	
	public List<Order> getOrdersHistoryAndStatus(String status);

	public List<Order> getOrdersForToday();
    
	public List<Order> getAllOrdersNotAffected();
	
	public List<Order> getAllOrdersAffected();
	
	public List<Order> getAllOrdersNotAffectedBySource(OrderSource orderSource);
	
	public Order cancelOrder(UUID id, String reason, String subject, MultipartFile attachment)throws Exception;
    
	public List<Order> getOrdersForOrganization(OrganizationEntity organizationEntity);
	
	public List<Order> getOrdersDealProForOrganization(OrganizationEntity organizationEntity,OrderStatus orderStatus);
	


}
