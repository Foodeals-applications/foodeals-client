package net.foodeals.offer.infrastructure.interfaces.web;
import net.foodeals.core.domain.entities.Offer;
import net.foodeals.core.domain.enums.BoxType;
import net.foodeals.offer.application.dtos.responses.*;
import net.foodeals.offer.application.services.BoxService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("v1/boxs")
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;
    private final ModelMapper mapper;
    @GetMapping("/details/{id}")
    public ResponseEntity<BoxDetailsResponse> getDetailsBox(@PathVariable UUID id) {
        BoxDetailsResponse response = boxService.getBoxDetails(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    public ResponseEntity<FeaturedBoxesResponse> getFeaturedBoxes() {
        return ResponseEntity.ok(boxService.getFeaturedBoxes());
    }

    @GetMapping("/list-box-surprise")
    public ResponseEntity<List<Offer>> getBoxSurprise(@RequestParam(defaultValue = "0") Integer pageNum,
                                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Offer>offers= boxService.findOffersByBoxType(BoxType.MYSTERY_BOX);
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/list")
    public BoxListResponse listBoxes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "all") String category
    ) {
        return boxService.getBoxes(page, limit, category);
    }

    @GetMapping("/categories")
    public List<BoxCategory> listCategories() {
        return boxService.getAllCategories();
    }


}
