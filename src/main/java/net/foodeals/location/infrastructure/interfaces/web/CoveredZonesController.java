package net.foodeals.location.infrastructure.interfaces.web;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.delivery.application.services.impl.CoveredZonesService;
import net.foodeals.location.application.dtos.responses.CoveredZoneResponse;

@RestController
@RequestMapping("v1/covered-zones")
@RequiredArgsConstructor
public class CoveredZonesController {

	private final CoveredZonesService service;
	private final ModelMapper mapper;

	@GetMapping
	public ResponseEntity<List<CoveredZoneResponse>> getAll() {
		final List<CoveredZoneResponse> responses = service.findAll().stream()
				.map(covered -> mapper.map(covered, CoveredZoneResponse.class)).toList();
		return ResponseEntity.ok(responses);
	}

}
