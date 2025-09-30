package net.foodeals.offer.application.services;

import java.util.List;
import java.util.UUID;

import net.foodeals.offer.application.dtos.responses.DealDetailsResponse;
import net.foodeals.offer.application.dtos.responses.FeaturedDealsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import net.foodeals.common.contracts.CrudService;
import net.foodeals.offer.application.dtos.requests.DealDto;
import net.foodeals.offer.application.dtos.requests.DealProDto;
import net.foodeals.offer.application.dtos.requests.RelaunchDealDto;
import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.organizationEntity.domain.entities.SubEntity;

public interface DealService extends CrudService<Deal, UUID, DealDto>{

    DealDetailsResponse getDetailsDeal(UUID id);

    public FeaturedDealsResponse getFeaturedDeals();
	
	
}
