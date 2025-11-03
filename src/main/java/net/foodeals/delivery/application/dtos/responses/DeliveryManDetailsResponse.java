package net.foodeals.delivery.application.dtos.responses;

import net.foodeals.core.domain.enums.ModalityPaiement;

import java.util.Date;


public record DeliveryManDetailsResponse(Integer id, String lastName, String firstName,
                                         Integer distance , String deliveryAddress, Date dateOfOrder,
                                         ModalityPaiement modalityPayment) {

}
