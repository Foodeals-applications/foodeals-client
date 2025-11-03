package net.foodeals.product.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import net.foodeals.core.domain.entities.ProductCategory;
import net.foodeals.core.exceptions.ProductCategoryNotFoundException;
import net.foodeals.core.repositories.ProductCategoryRepository;
import net.foodeals.product.application.dtos.requests.ProductCategoryRequest;
import net.foodeals.product.application.services.ProductCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository repository;


    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<ProductCategory> findAll(Integer pageNumber, Integer pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public ProductCategory findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(id));
    }

    @Override
    public ProductCategory create(ProductCategoryRequest request) {


        final String slug =  null ;
        final ProductCategory productCategory = ProductCategory.create(request.name(), slug);

        return repository.save(productCategory);
    }

    @Override
    public ProductCategory update(UUID id, ProductCategoryRequest request) {
        final ProductCategory productCategory = findById(id);


        productCategory
                .setName(request.name())
                .setSlug(null)
                ;

        return repository.save(productCategory);
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id))
            throw new ProductCategoryNotFoundException(id);

        repository.softDelete(id);
    }
}
