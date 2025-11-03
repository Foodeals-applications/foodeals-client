package net.foodeals.businessrecomendations.application.service;

import lombok.RequiredArgsConstructor;
import net.foodeals.businessrecomendations.application.dtos.requests.BusinessRecommendationRequest;
import net.foodeals.businessrecomendations.application.dtos.responses.BusinessRecommendationResponse;
import net.foodeals.core.domain.entities.BusinessRecommendation;
import net.foodeals.core.repositories.BusinessRecommendationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessRecommendationService {

    private final BusinessRecommendationRepository repository;

    public BusinessRecommendationResponse createRecommendation(BusinessRecommendationRequest request) {
        BusinessRecommendation entity = new BusinessRecommendation();
        entity.setBusinessName(request.getBusinessName());
        entity.setAddress(request.getAddress());
        entity.setCategory(request.getCategory());
        entity.setDescription(request.getDescription());

        BusinessRecommendation saved = repository.save(entity);
        return BusinessRecommendationResponse.fromEntity(saved);
    }
}