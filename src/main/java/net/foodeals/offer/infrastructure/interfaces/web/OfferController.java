package net.foodeals.offer.infrastructure.interfaces.web;

import net.foodeals.offer.application.dtos.responses.OfferListResponse;
import net.foodeals.offer.application.services.OfferService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/list")
    public OfferListResponse getOffers(
            @RequestParam String type,
            @RequestParam double lat,
            @RequestParam double lng
    ) {
        return offerService.getOffers(type, lat, lng);
    }
}
