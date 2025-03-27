package net.foodeals.product.infrastructure.interfaces.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.responses.DeliveryMethodResponse;
import net.foodeals.product.application.services.DeliveryMethodService;

@RestController
@RequestMapping("v1/delivery-methods")
@RequiredArgsConstructor
public class DeliveryMethodController {

	private final DeliveryMethodService service;

	@GetMapping("/all")
	public ResponseEntity<List<DeliveryMethodResponse>> getAll() {
		List<DeliveryMethodResponse> response = service.findAll();

		return ResponseEntity.ok(response);
	}

}
