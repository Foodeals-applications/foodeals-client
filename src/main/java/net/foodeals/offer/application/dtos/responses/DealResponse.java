package net.foodeals.offer.application.dtos.responses;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.foodeals.offer.domain.enums.DealStatus;

public record DealResponse(UUID id, String productName, String productDescription, String productPhotoPath,
						   Date creationDate, Integer numberOfOrders, Integer numberOfItems, DealStatus dealStatus,
						   List<SupplementDealResponse>supplementDealResponses) {
}
