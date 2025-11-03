package net.foodeals.offer.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.Box;
import net.foodeals.core.domain.entities.Offer;
import net.foodeals.core.domain.enums.BoxType;
import net.foodeals.offer.application.dtos.requests.BoxDto;
import net.foodeals.offer.application.dtos.responses.BoxCategory;
import net.foodeals.offer.application.dtos.responses.BoxDetailsResponse;
import net.foodeals.offer.application.dtos.responses.BoxListResponse;
import net.foodeals.offer.application.dtos.responses.FeaturedBoxesResponse;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface BoxService extends CrudService<Box, UUID, BoxDto>{
    Box create(BoxDto dto);

    Box findById(UUID id);

    void delete(UUID id);
    
    List<Offer> findOffersByBoxType(BoxType boxType);
    
    Page<Box> findAllNormalBox(Integer pageNumber, Integer pageSize);
    
	Page<Box> findAllSurpriseBox(Integer pageNumber, Integer pageSize);
	
	String saveFile(MultipartFile file);

    public BoxListResponse getBoxes(int page, int limit, String categoryStr);

    public List<BoxCategory> getAllCategories();

    void deleteBox(UUID id, String reason, String motif);
	
	Page<Box> getExpiredAndUnavailableBoxs(Pageable pageable,BoxType type);

	Object findByIdAndType(UUID id, BoxType typeBox);
	
	Box createBox(BoxDto dto,BoxType type);

	Box updateBox(UUID id, BoxDto updatedDto, BoxType type);
	
	Box relaunchBox(UUID id);

	BoxDetailsResponse getBoxDetails(UUID id);

    public FeaturedBoxesResponse getFeaturedBoxes();

    
}
