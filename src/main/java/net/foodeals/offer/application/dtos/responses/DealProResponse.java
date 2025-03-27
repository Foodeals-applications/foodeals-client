package net.foodeals.offer.application.dtos.responses;

import java.util.Date;
import java.util.List;

import net.foodeals.offer.application.dtos.requests.OpenTimeDto;
import net.foodeals.offer.application.dtos.requests.PriceBlockDTO;
import net.foodeals.offer.domain.entities.Deal.DealUnityType;
import net.foodeals.offer.domain.enums.Category;
import net.foodeals.offer.domain.enums.DealStatus;
import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.offer.domain.enums.PublishAs;

public record DealProResponse (Integer quantity, ProductDealResponse productResponse,Date expirationDate,
		String organizationName,String organizationAvatar,PublishAs publishAs, Category category, DealStatus dealStatus,
		DealUnityType dealUnityType, List<PriceBlockDTO> defaultPrices, List<PriceBlockDTO> customPrices,List<ModalityType> modalityTypes, 
		ModalityPaiement modalityPaiement,Long deliveryFee,List<OpenTimeDto>openTimes) {
	
}


