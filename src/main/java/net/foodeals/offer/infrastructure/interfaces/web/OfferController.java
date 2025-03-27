package net.foodeals.offer.infrastructure.interfaces.web;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.requests.OfferRequest;
import net.foodeals.offer.application.dtos.responses.OfferResponse;
import net.foodeals.offer.application.services.BoxService;
import net.foodeals.offer.application.services.DealService;
import net.foodeals.offer.application.services.OfferService;
import net.foodeals.offer.domain.enums.BoxType;

@RestController
@RequestMapping("v1/offers")
@RequiredArgsConstructor
public class OfferController {

	private final OfferService service;
	private final BoxService boxService;
	private final DealService dealService;
	private final ModelMapper mapper;

	@GetMapping
	public ResponseEntity<Page<OfferResponse>> getAll(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		final Page<OfferResponse> response = service.findAll(pageNum, pageSize)
				.map(offer -> mapper.map(offer, OfferResponse.class));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/search")
	public ResponseEntity<List<OfferResponse>> searchOffersByType(@RequestParam("type") String type) {

		List<OfferResponse> offersResponses;
		try {

			if ("DEAL".equalsIgnoreCase(type)) {
				offersResponses = dealService.findAllDealsOffers().stream()
						.map(offer -> mapper.map(offer, OfferResponse.class)).collect(Collectors.toList());
			}

			else

			if ("NORMAL_BOX".equalsIgnoreCase(type)) {
				offersResponses = boxService.findOffersByBoxType(BoxType.NORMAL_BOX).stream()
						.map(offer -> mapper.map(offer, OfferResponse.class)).collect(Collectors.toList());
			} else if ("MYSTERY_BOX".equalsIgnoreCase(type)) {
				offersResponses = boxService.findOffersByBoxType(BoxType.MYSTERY_BOX).stream()
						.map(offer -> mapper.map(offer, OfferResponse.class)).collect(Collectors.toList());

			} else {
				return ResponseEntity.badRequest().build();
			}

			return ResponseEntity.ok(offersResponses);
		} catch (Exception e) {
			return ResponseEntity.status(500).build();
		}
	}
	
	@GetMapping("/search/historic")
	public ResponseEntity<List<OfferResponse>> searchHistoricsOffersByType(@RequestParam("type") String type) {

		List<OfferResponse> offersResponses;
		try {

			if ("DEAL".equalsIgnoreCase(type)) {
				offersResponses = dealService.findAllHistoricsDealsOffers().stream()
						.map(offer -> mapper.map(offer, OfferResponse.class)).collect(Collectors.toList());
			}

			/*else

			if ("NORMAL_BOX".equalsIgnoreCase(type)) {
				offersResponses = boxService.findOffersByBoxType(BoxType.NORMAL_BOX).stream()
						.map(offer -> mapper.map(offer, OfferResponse.class)).collect(Collectors.toList());
			} else if ("MYSTERY_BOX".equalsIgnoreCase(type)) {
				offersResponses = boxService.findOffersByBoxType(BoxType.MYSTERY_BOX).stream()
						.map(offer -> mapper.map(offer, OfferResponse.class)).collect(Collectors.toList());

			} */else {
				return ResponseEntity.badRequest().build();
			}

			return ResponseEntity.ok(offersResponses);
		} catch (Exception e) {
			return ResponseEntity.status(500).build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<OfferResponse> getById(@PathVariable UUID id) {
		final OfferResponse response = mapper.map(service.findById(id), OfferResponse.class);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<OfferResponse> create(@RequestBody @Valid OfferRequest request) {
		final OfferResponse response = mapper.map(service.create(request), OfferResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<OfferResponse> update(@PathVariable UUID id, @RequestBody @Valid OfferRequest request) {
		final OfferResponse response = mapper.map(service.update(id, request), OfferResponse.class);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
