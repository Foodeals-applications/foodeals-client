package net.foodeals.offer.infrastructure.interfaces.web;
import net.foodeals.offer.application.dtos.responses.BoxDetailsResponse;
import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.offer.application.services.BoxService;
import net.foodeals.offer.domain.entities.Cart;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@RestController
@RequestMapping("v1/boxs")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;
    @GetMapping("/details/{id}")
    public ResponseEntity<BoxDetailsResponse> getDetailsBox(@PathVariable UUID id) {
        BoxDetailsResponse response = boxService.getBoxDetails(id);
        return ResponseEntity.ok(response);
    }


}
