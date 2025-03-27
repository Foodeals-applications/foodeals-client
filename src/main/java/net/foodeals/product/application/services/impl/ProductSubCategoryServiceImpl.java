package net.foodeals.product.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.requests.ProductSubCategoryRequest;
import net.foodeals.product.application.services.ProductSubCategoryService;
import net.foodeals.product.domain.entities.ProductSubCategory;
import net.foodeals.product.domain.exceptions.ProductCategoryNotFoundException;
import net.foodeals.product.domain.exceptions.ProductSubCategoryNotFoundException;
import net.foodeals.product.domain.repositories.ProductSubCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static net.foodeals.common.Utils.SlugUtil.makeUniqueSlug;
import static net.foodeals.common.Utils.SlugUtil.toSlug;

@Service
@Transactional
@RequiredArgsConstructor
class ProductSubCategoryServiceImpl implements ProductSubCategoryService {

    private final ProductSubCategoryRepository repository;


    @Override
    public List<ProductSubCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<ProductSubCategory> findAll(Integer pageNumber, Integer pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public ProductSubCategory findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException(id));
    }

    @Override
    public ProductSubCategory create(ProductSubCategoryRequest request) {


        final String slug = makeUniqueSlug(toSlug(request.name()), repository);
        final ProductSubCategory productSubCategory = ProductSubCategory.create(request.name(), slug);

        return repository.save(productSubCategory);
    }

    @Override
    public ProductSubCategory update(UUID id, ProductSubCategoryRequest request) {
        final ProductSubCategory productSubCategory = findById(id);


        productSubCategory
                .setName(request.name())
                .setSlug(makeUniqueSlug(toSlug(request.name()), repository))
                ;

        return repository.save(productSubCategory);
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id))
            throw new ProductSubCategoryNotFoundException(id);

        repository.softDelete(id);
    }
}
