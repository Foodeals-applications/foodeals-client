package net.foodeals.product.infrastructure.interfaces.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.responses.PickupConditionResponse;
import net.foodeals.product.application.services.PickupConditionService;

@RestController
@RequestMapping("v1/pickup-conditions")
@RequiredArgsConstructor
public class PickupConditionController {

	private final PickupConditionService service;

	@GetMapping("/all")
	public ResponseEntity<List<PickupConditionResponse>> getAll() {
		List<PickupConditionResponse> response = service.findAll();

		return ResponseEntity.ok(response);
	}

}
