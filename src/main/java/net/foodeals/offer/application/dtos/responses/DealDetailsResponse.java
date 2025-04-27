package net.foodeals.offer.application.dtos.responses;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DealDetailsResponse (UUID id ,String dealTitle,String productName ,
								   String productPhoto ,String addressSubEntity,
		BigDecimal newPrice,BigDecimal oldPrice,
		Integer reduction ,Double distance ,List<SupplementDealResponse>supplementDealResponses) {



}
