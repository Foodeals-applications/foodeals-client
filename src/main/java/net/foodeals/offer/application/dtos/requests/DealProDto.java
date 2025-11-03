package net.foodeals.offer.application.dtos.requests;

import java.util.List;

import net.foodeals.core.domain.entities.Deal;
import net.foodeals.core.domain.enums.*;
import net.foodeals.product.application.dtos.requests.ProductRequest;

public record DealProDto(Integer quantity, ProductRequest productRequest,
                         PublishAs publishAs, Category category, DealStatus dealStatus,
                         Deal.DealUnityType dealUnityType, List<PriceBlockDTO> defaultPrices, List<PriceBlockDTO> customPrices, List<ModalityType> modalityTypes,
                         ModalityPaiement modalityPaiement, Long deliveryFee) {
	
}
