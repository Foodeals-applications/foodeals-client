package net.foodeals.offer.application.dtos.requests;

import java.util.List;

import net.foodeals.offer.domain.entities.Deal.DealUnityType;
import net.foodeals.offer.domain.enums.Category;
import net.foodeals.offer.domain.enums.DealStatus;
import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.offer.domain.enums.PublishAs;
import net.foodeals.product.application.dtos.requests.ProductRequest;

public record DealProDto(Integer quantity, ProductRequest productRequest,
		PublishAs publishAs, Category category, DealStatus dealStatus,
		DealUnityType dealUnityType, List<PriceBlockDTO> defaultPrices, List<PriceBlockDTO> customPrices,List<ModalityType> modalityTypes, 
		ModalityPaiement modalityPaiement,Long deliveryFee) {
	
}
