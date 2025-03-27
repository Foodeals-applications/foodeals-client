package net.foodeals.product.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.common.contracts.SlugRepository;
import net.foodeals.product.domain.entities.ProductCategory;

public interface ProductCategoryRepository extends BaseRepository<ProductCategory, UUID>,SlugRepository {

	@Query("SELECT c FROM ProductCategory c WHERE c.name = :name AND c.deletedAt IS NULL")
	public Optional<ProductCategory>  findByName(String name);
	
	@Query("SELECT c FROM ProductCategory c WHERE c.slug = :slug AND c.deletedAt IS NULL")
    Optional<ProductCategory> findBySlug(String slug);
}

