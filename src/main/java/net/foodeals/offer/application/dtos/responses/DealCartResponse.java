package net.foodeals.offer.application.dtos.responses;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.foodeals.common.valueOjects.Price;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.offer.domain.enums.ModalityType;

public record DealCartResponse (UUID id , 
		String titleDeal,String description,Date dateDeal,LocalTime hourOfDeal,
		String organizationName,String organizationAvatar,Price price,Integer quantity,
		Date orderDate,
		ModalityPaiement modalityPayment,
		List<ModalityType>modalityTypes,
		String deliveryAdress) {

}
