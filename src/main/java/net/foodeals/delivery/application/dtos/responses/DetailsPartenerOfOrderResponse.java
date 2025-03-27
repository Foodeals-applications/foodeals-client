package net.foodeals.delivery.application.dtos.responses;

import java.util.Date;
import java.util.UUID;

public record DetailsPartenerOfOrderResponse (UUID idOrder, String partnerName,String partnerAvatar,String deliveryAddress,
		Date orderDate , String modalityPayment) {

}
