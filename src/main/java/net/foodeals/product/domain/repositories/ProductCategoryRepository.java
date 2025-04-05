package net.foodeals.product.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.common.contracts.SlugRepository;
import net.foodeals.product.application.dtos.responses.CategoryWithOffersDTO;
import net.foodeals.product.application.dtos.responses.ProductCategoryDTO;
import net.foodeals.product.domain.entities.ProductCategory;

public interface ProductCategoryRepository extends BaseRepository<ProductCategory, UUID>,SlugRepository {

	@Query("SELECT c FROM ProductCategory c WHERE c.name = :name AND c.deletedAt IS NULL")
	public Optional<ProductCategory>  findByName(String name);
	
	@Query("SELECT c FROM ProductCategory c WHERE c.slug = :slug AND c.deletedAt IS NULL")
    Optional<ProductCategory> findBySlug(String slug);
	
	
	/*@Query("SELECT new net.foodeals.dto.CategoryWithOffersDTO(pc.name, pc.isFavorite) FROM ProductCategory pc WHERE pc.subentity.id = :subentityId")
    List<CategoryWithOffersDTO> getCategoriesWithActiveOffers(@Param("subentityId") UUID subentityId);
    
	@Query("SELECT new net.foodeals.dto.ProductCategoryDTO(pc.name, p) FROM ProductCategory pc JOIN pc.products p WHERE pc.subentity.id = :subentityId")
    List<ProductCategoryDTO> getProductsByCategory(@Param("subentityId") UUID subentityId);*/


	@Query("""
        SELECT DISTINCT c.name
        FROM ProductCategory c
        JOIN c.products p
        WHERE p.subEntity.id = :subEntityId
    """)
	List<String> findActiveCategoryNamesBySubEntity(UUID subEntityId);

	@Query("""
        SELECT c
        FROM ProductCategory c
        JOIN c.products p
        WHERE p.subEntity.id = :subEntityId
    """)
	List<ProductCategory> findCategoriesBySubEntity(UUID subEntityId);


}

