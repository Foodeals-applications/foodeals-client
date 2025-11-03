package net.foodeals.product.infrastructure.interfaces.web;

import net.foodeals.core.repositories.SupplementRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.responses.SupplementDealResponse;

@RestController
@RequestMapping("v1/supplements")
@RequiredArgsConstructor
public class SupplementController {

	private final SupplementRepository supplementRepository;
	private final ModelMapper mapper;

	@GetMapping
	public ResponseEntity<Page<SupplementDealResponse>> getAll(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		Page<SupplementDealResponse> response = null;

		response = supplementRepository.findAll(PageRequest.of(pageNum, pageSize))
				.map(supplement -> mapper.map(supplement, SupplementDealResponse.class));

		return ResponseEntity.ok(response);
	}

}
