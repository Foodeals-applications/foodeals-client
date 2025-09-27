package net.foodeals.businessrecomendations.infrastructure;

import lombok.RequiredArgsConstructor;
import net.foodeals.businessrecomendations.application.dtos.requests.BusinessRecommendationRequest;
import net.foodeals.businessrecomendations.application.dtos.responses.BusinessRecommendationResponse;
import net.foodeals.businessrecomendations.application.service.BusinessRecommendationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/recommendations")
@RequiredArgsConstructor
public class BusinessRecommendationController {

    private final BusinessRecommendationService service;

    @PostMapping("/business")
    public BusinessRecommendationResponse
    create(@RequestBody BusinessRecommendationRequest request) {
        return service.createRecommendation(request);
    }
}