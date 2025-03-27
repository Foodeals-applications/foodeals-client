package net.foodeals.product.infrastructure.interfaces.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.requests.ProductCategoryRequest;
import net.foodeals.product.application.dtos.responses.ProductCategoryResponse;
import net.foodeals.product.application.services.ProductCategoryService;
import net.foodeals.product.domain.entities.ProductCategory;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

	private final ProductCategoryService service;
	private final ModelMapper mapper;

	@GetMapping
	public ResponseEntity<Page<ProductCategoryResponse>> getAll(@RequestParam("0") Integer pageNum,
			@RequestParam("10") Integer pageSize) {
		final Page<ProductCategoryResponse> response = service.findAll(pageNum, pageSize)
				.map(category -> mapper.map(category, ProductCategoryResponse.class));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/all")
	public ResponseEntity<List<ProductCategoryResponse>> getAll() {
		List<ProductCategory> categories = service.findAll();
		List<ProductCategoryResponse> response = categories.stream()
				.map(category -> mapper.map(category, ProductCategoryResponse.class)).collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductCategoryResponse> getById(@PathVariable UUID id) {
		final ProductCategoryResponse response = mapper.map(service.findById(id), ProductCategoryResponse.class);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<ProductCategoryResponse> create(@RequestBody ProductCategoryRequest request) {
		final ProductCategoryResponse response = mapper.map(service.create(request), ProductCategoryResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ProductCategoryResponse> update(@PathVariable UUID id,
			@RequestBody @Valid ProductCategoryRequest request) {
		final ProductCategoryResponse response = mapper.map(service.update(id, request), ProductCategoryResponse.class);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
