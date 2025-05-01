package net.foodeals.organizationEntity.application.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.foodeals.organizationEntity.application.dtos.responses.*;
import net.foodeals.user.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.organizationEntity.application.dtos.requests.SubEntityRequest;
import net.foodeals.organizationEntity.domain.entities.SubEntity;

@Service
public interface SubEntityService extends CrudService<SubEntity, UUID, SubEntityRequest> {

    public String saveFile(MultipartFile file);

    public SubEntity updateSubEntity(SubEntityRequest dto);

    void deleteSubEntity(UUID id, String reason, String motif);

    Page<SubEntity> filterSubEntities(Instant startDate, Instant endDate, String raisonSociale, UUID managerId,
                                      String email, String phone, UUID cityId, UUID solutionId,
                                      Pageable pageable);

    List<BestSellerResponse> getBestSellers(Double salesThreshold);


    Page<SubEntity> getAllByStatus(String status, Pageable pageable);

    SubEntity confirmSubEntity(UUID id);


    List<Map<String, Object>> getStoreCountByDomains();

    SubEntityDetailsResponse getSubEntityDetails(UUID subEntityId, Integer userId);

    HotelDetailsResponse getHotelDetails(UUID subEntityId);

    RestaurantDetailsResponse getRestaurantDetails(UUID subEntityId);


    BakeryDetailsResponse getBakeryDetails(UUID subEntityId);

    IndustryDetailsResponse getIndustryDetails(UUID subEntityId);

    AgriculturDetailsResponse getAgricultureDetails(UUID subEntityId);

    List<RestaurantResponse> getListOfRestaurants(User user, double radius);

}

    
