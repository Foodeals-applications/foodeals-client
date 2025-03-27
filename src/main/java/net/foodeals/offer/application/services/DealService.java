package net.foodeals.offer.application.services;

import java.util.List;
import java.util.UUID;

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

    Deal create(DealDto dto);

    Deal update(UUID id, DealDto dto);

    Deal findById(UUID id);

    void delete(UUID id);
    
    List<Offer> findAllDealsOffers();
    
    List<Offer>  findAllHistoricsDealsOffers();
    
    String saveFile(MultipartFile file);

	void deleteDeal(UUID id, String reason, String motif);
	
	
	Page<Deal> getExpiredAndUnavailableDeals(Pageable pageable);

	Deal relaunchDeal(@Valid RelaunchDealDto dto,UUID id);

	Page<Deal> findDealPro(Pageable pageable);
	
	Deal createPro(DealProDto dealProDTO,String photoPath) ;
	
	
}
