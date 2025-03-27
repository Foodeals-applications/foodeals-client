package net.foodeals.product.infrastructure.interfaces.web;

import java.util.UUID;

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
import net.foodeals.product.application.dtos.requests.ProductSubCategoryRequest;
import net.foodeals.product.application.dtos.responses.ProductSubCategoryResponse;
import net.foodeals.product.application.services.ProductSubCategoryService;

@RestController
@RequestMapping("v1/product-subcategories")
@RequiredArgsConstructor
public class ProductSubCategoryController {

    private final ProductSubCategoryService service;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Page<ProductSubCategoryResponse>> getAll(@RequestParam("0") Integer pageNum, @RequestParam("10") Integer pageSize) {
        final Page<ProductSubCategoryResponse> response = service.findAll(pageNum, pageSize)
                .map(category -> mapper.map(category, ProductSubCategoryResponse.class));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSubCategoryResponse> getById(@PathVariable UUID id) {
        final ProductSubCategoryResponse response = mapper.map(
                service.findById(id),
                ProductSubCategoryResponse.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductSubCategoryResponse> create(@RequestBody ProductSubCategoryRequest request) {
        final ProductSubCategoryResponse response = mapper.map(
                service.create(request),
                ProductSubCategoryResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductSubCategoryResponse> update(@PathVariable UUID id, @RequestBody @Valid ProductSubCategoryRequest request) {
        final ProductSubCategoryResponse response = mapper.map(
                service.update(id, request),
                ProductSubCategoryResponse.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
