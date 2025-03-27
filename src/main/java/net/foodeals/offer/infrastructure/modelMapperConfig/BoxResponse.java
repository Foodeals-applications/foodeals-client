package net.foodeals.offer.infrastructure.modelMapperConfig;

import java.util.Date;
import java.util.UUID;
import net.foodeals.offer.domain.enums.BoxStatus;
import net.foodeals.offer.domain.enums.BoxType;

public record BoxResponse(UUID id, String titleBox,String descriptionBox,BoxType boxType,String photoBox,
		Date creationDate,Integer numberOfOrders,Integer numberOfItems,BoxStatus boxStatus,String type) {
	
}



