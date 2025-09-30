package net.foodeals.offer.infrastructure.interfaces.web;


import java.util.UUID;

import net.foodeals.offer.application.dtos.responses.FeaturedDealsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.responses.DealDetailsResponse;
import net.foodeals.offer.application.services.DealService;

@RestController
@RequestMapping("v1/deals")
@RequiredArgsConstructor
public class DealController {

	private final DealService service;


    @GetMapping("/featured")
    public ResponseEntity<FeaturedDealsResponse> getFeaturedDeals() {
        return ResponseEntity.ok(service.getFeaturedDeals());
    }

	@GetMapping("/details/{id}")
	public ResponseEntity<DealDetailsResponse> get(@PathVariable UUID id) {
		DealDetailsResponse response = service.getDetailsDeal(id);
		return ResponseEntity.ok(response);
	}


}
