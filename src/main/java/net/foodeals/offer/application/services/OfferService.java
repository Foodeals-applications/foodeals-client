package net.foodeals.offer.application.services;

import java.util.Map;
import java.util.UUID;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.Offer;
import net.foodeals.offer.application.dtos.requests.OfferRequest;
import net.foodeals.offer.application.dtos.responses.OfferListResponse;

public interface OfferService extends CrudService<Offer, UUID, OfferRequest> {
	
	public Map<String, Object> getNears(double userLat, double userLon, double radius);

    public OfferListResponse getOffers(String type, double lat, double lng);

}
