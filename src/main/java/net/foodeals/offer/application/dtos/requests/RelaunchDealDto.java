package net.foodeals.offer.application.dtos.requests;

import java.util.List;

import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.offer.domain.enums.ModalityType;

public record RelaunchDealDto(
		ModalityType modalityType, 
		ModalityPaiement modalityPaiement,Long deliveryFee,List<OpenTimeDto> openTimes) {
}
