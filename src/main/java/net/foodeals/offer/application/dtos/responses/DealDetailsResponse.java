package net.foodeals.offer.application.dtos.responses;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import net.foodeals.offer.application.dtos.requests.OpenTimeDto;
import net.foodeals.offer.domain.entities.Deal.DealUnityType;
import net.foodeals.offer.domain.enums.Category;
import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.offer.domain.enums.PublishAs;

public record DealDetailsResponse (UUID id ,ProductDealResponse produtDealResponse , Integer quantity, 
		BigDecimal price,
		DealUnityType dealUnityType,
		List<SupplementDealResponse> supplements,   BigDecimal salePrice, 
		Integer reduction,List<ModalityType> modalityTypes, 
		ModalityPaiement modalityPaiement,Long deliveryFee,List<OpenTimeDto> openTimes,
		PublishAs publishAs,Category category) {



}
