package net.foodeals.product.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.common.contracts.SlugRepository;
import net.foodeals.product.domain.entities.ProductSubCategory;

public interface ProductSubCategoryRepository extends BaseRepository<ProductSubCategory, UUID> ,
        SlugRepository {
	
	@Query("SELECT sc FROM ProductSubCategory sc WHERE sc.name = :name AND sc.deletedAt IS NULL")
	public Optional<ProductSubCategory>  findByName(String name);
	
	
	@Query("SELECT c FROM ProductSubCategory c WHERE c.slug = :slug AND c.deletedAt IS NULL")
    Optional<ProductSubCategory> findBySlug(String slug);
}

