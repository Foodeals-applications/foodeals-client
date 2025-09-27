package net.foodeals.businessrecomendations.application.dtos.responses;


import lombok.Data;
import net.foodeals.businessrecomendations.domain.entities.BusinessRecommendation;

import java.time.Instant;
import java.util.UUID;

@Data
public class BusinessRecommendationResponse {
    private UUID id;
    private String businessName;
    private String address;
    private String category;
    private String description;
    private Instant createdAt;

    public static BusinessRecommendationResponse fromEntity(BusinessRecommendation entity) {
        BusinessRecommendationResponse dto = new BusinessRecommendationResponse();
        dto.setId(entity.getId());
        dto.setBusinessName(entity.getBusinessName());
        dto.setAddress(entity.getAddress());
        dto.setCategory(entity.getCategory());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
