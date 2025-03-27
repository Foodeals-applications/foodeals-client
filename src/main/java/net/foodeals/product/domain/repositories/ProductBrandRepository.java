package net.foodeals.product.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.common.contracts.SlugRepository;
import net.foodeals.product.domain.entities.ProductBrand;

public interface ProductBrandRepository extends BaseRepository<ProductBrand, UUID>,SlugRepository {


	@Query("SELECT b FROM ProductBrand b WHERE b.name = :name AND b.deletedAt IS NULL")
	public Optional<ProductBrand>  findByName(String name);
}
