package net.foodeals.businessrecomendations.application.dtos.requests;

import lombok.Data;

@Data
public class BusinessRecommendationRequest {
    private String businessName;
    private String address;
    private String category;
    private String description;
}