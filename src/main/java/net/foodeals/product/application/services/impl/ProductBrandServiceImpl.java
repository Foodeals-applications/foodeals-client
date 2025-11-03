package net.foodeals.product.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.ProductBrand;
import net.foodeals.core.exceptions.ProductBrandNotFoundException;
import net.foodeals.core.exceptions.ProductCategoryNotFoundException;
import net.foodeals.core.repositories.ProductBrandRepository;
import net.foodeals.product.application.dtos.requests.ProductBrandRequest;
import net.foodeals.product.application.services.ProductBrandService;
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
class ProductBrandServiceImpl implements ProductBrandService {

    private final ProductBrandRepository repository;


    @Override
    public List<ProductBrand> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<ProductBrand> findAll(Integer pageNumber, Integer pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public ProductBrand findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductBrandNotFoundException(id));
    }

    @Override
    public ProductBrand create(ProductBrandRequest request) {


        final String slug = null;
        final ProductBrand productBrand = ProductBrand.create(request.name(), slug);

        return repository.save(productBrand);
    }

    @Override
    public ProductBrand update(UUID id, ProductBrandRequest request) {
        final ProductBrand productBrand = findById(id);


        productBrand
                .setName(request.name())
                .setSlug(null)
                ;

        return repository.save(productBrand);
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id))
            throw new ProductCategoryNotFoundException(id);

        repository.softDelete(id);
    }

	@Override
	public ProductBrand findByName(String name) {
		return repository.findByName(name).orElseThrow(() -> new ProductBrandNotFoundException(name));
	}
}
