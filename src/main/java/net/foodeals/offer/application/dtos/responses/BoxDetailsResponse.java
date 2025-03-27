package net.foodeals.offer.application.dtos.responses;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.valueOjects.Price;
import net.foodeals.offer.domain.enums.Category;
import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.offer.domain.enums.PublishAs;

public record BoxDetailsResponse (UUID id ,String photoPath,String title ,
		PublishAs publishAs,Category category,List<OpenTimeResponse> openTime,
		String description,Integer quantity,List<ModalityType> modalityTypes, ModalityPaiement modalityPayment,Price initialPrice,
		Integer reduction,Price salePrice,Long deliveryFee){

}
