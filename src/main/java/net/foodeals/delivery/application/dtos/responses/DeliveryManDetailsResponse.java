package net.foodeals.delivery.application.dtos.responses;

import java.util.Date;

import net.foodeals.offer.domain.enums.ModalityPaiement;

public record DeliveryManDetailsResponse(Integer id,String lastName,String firstName,
		Integer distance ,String deliveryAddress,Date dateOfOrder,ModalityPaiement modalityPayment) {

}
