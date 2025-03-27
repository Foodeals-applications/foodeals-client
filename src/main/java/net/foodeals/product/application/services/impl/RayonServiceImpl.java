package net.foodeals.product.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import net.foodeals.product.application.dtos.requests.ProductCategoryRequest;
import net.foodeals.product.application.dtos.requests.RayonRequest;
import net.foodeals.product.application.services.ProductCategoryService;
import net.foodeals.product.application.services.RayonService;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.entities.Rayon;
import net.foodeals.product.domain.exceptions.ProductCategoryNotFoundException;
import net.foodeals.product.domain.repositories.ProductCategoryRepository;
import net.foodeals.product.domain.repositories.RayonRepository;

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
class RayonServiceImpl implements RayonService {

	private final RayonRepository repository;

	@Override
	public List<Rayon> findAll() {
		return repository.findAll();
	}

	@Override
	public Page<Rayon> findAll(Integer pageNumber, Integer pageSize) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public Rayon findById(UUID id) {
		return repository.findById(id).orElseThrow(() -> new ProductCategoryNotFoundException(id));
	}

	@Override
	public Rayon create(RayonRequest request) {

		final Rayon rayon = Rayon.create(request.name());

		return repository.save(rayon);
	}

	@Override
	public Rayon update(UUID id, RayonRequest request) {
		final Rayon rayon = findById(id);

		rayon.setName(request.name())

		;

		return repository.save(rayon);
	}

	@Override
	public void delete(UUID id) {
		if (!repository.existsById(id))
			throw new ProductCategoryNotFoundException(id);

		repository.softDelete(id);
	}
}
