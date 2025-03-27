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
import net.foodeals.product.application.dtos.requests.ProductBrandRequest;
import net.foodeals.product.application.dtos.responses.ProductBrandResponse;
import net.foodeals.product.application.services.ProductBrandService;

@RestController
@RequestMapping("v1/brands")
@RequiredArgsConstructor
public class ProductBrandController {

    private final ProductBrandService service;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Page<ProductBrandResponse>> getAll(@RequestParam(defaultValue = "0") 
    Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        final Page<ProductBrandResponse> response = service.findAll(pageNum, pageSize)
                .map(brand -> mapper.map(brand, ProductBrandResponse.class));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBrandResponse> getById(@PathVariable UUID id) {
        final ProductBrandResponse response = mapper.map(
                service.findById(id),
                ProductBrandResponse.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductBrandResponse> create(@RequestBody ProductBrandRequest request) {
        final ProductBrandResponse response = mapper.map(
                service.create(request),
                ProductBrandResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductBrandResponse> update(@PathVariable UUID id, @RequestBody @Valid ProductBrandRequest request) {
        final ProductBrandResponse response = mapper.map(
                service.update(id, request),
                ProductBrandResponse.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
