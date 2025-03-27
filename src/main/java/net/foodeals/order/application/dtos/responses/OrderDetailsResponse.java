package net.foodeals.order.application.dtos.responses;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.foodeals.common.valueOjects.Price;
import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.user.domain.valueObjects.Name;

public record OrderDetailsResponse(UUID id, 
		Integer quantity, 
		Date orderDate,
		LocalTime hourOfOrder,
		List<String> photosProducts,
		String title,
		String description, 
		Price priceOrder, 
		Integer quantityOfOrder,
		Name client,
		String clientAvatar,
		String phoneClient,
		String emailClient,
		String clientActivity,		
		String sellerName,
		String sellerAvatar,
		String sellerActivity,
		String sellerContact,
		String typePayment,
		String deliveryPartenerName,
		Date deliveryDate , 
		LocalTime hourOfDelivet ,
		Name DeliveryBoyName,
		String deliveryBoyPhone,
		String deliveryBoyEmail, 
		String deliveryAdress,
		OrderStatus orderStatus,
		String cancellationReason,
		String  cancellationSubject,
		String attechementFile,
		List<ModalityType> modality) {}
