package net.foodeals.offer.domain.repositories;

import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.organizationEntity.domain.entities.SubEntity;

public interface OfferRepository extends BaseRepository<Offer, UUID> {
	
	List<Offer>getOffersBySubEntity(SubEntity subEntity);
}
