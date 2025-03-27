package net.foodeals.product.infrastructure.interfaces.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.responses.PaymentMethodResponse;
import net.foodeals.product.application.services.PaymentMethodService;

@RestController
@RequestMapping("v1/paiments-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

	private final PaymentMethodService service;

	@GetMapping("/all")
	public ResponseEntity<List<PaymentMethodResponse>> getAll() {
		List<PaymentMethodResponse> response = service.findAll();

		return ResponseEntity.ok(response);
	}

}
