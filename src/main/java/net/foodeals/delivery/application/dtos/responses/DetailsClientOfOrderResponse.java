package net.foodeals.delivery.application.dtos.responses;

import java.util.Date;
import java.util.UUID;

public record DetailsClientOfOrderResponse (UUID idOrder, String firstName,String lastName,String deliveryAddress,
		Date orderDate , String modalityPayment) {

}
