package net.foodeals.product.domain.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.common.contracts.SlugRepository;
import net.foodeals.product.application.dtos.responses.ProductWithOfferDTO;
import net.foodeals.product.domain.entities.Product;

public interface ProductRepository extends BaseRepository<Product, UUID>, SlugRepository {

	@Query("SELECT p FROM Product p WHERE p.barcode = ?1 AND p.deletedAt IS NULL")
	public Optional<Product> findByBarcode(String barcode);

	@Query("SELECT p FROM Product p WHERE p.name ILIKE CONCAT(?1, '%') AND p.deletedAt IS NULL")
	public Page<Product> findProductsByName(String name, Pageable pageable);

	@Query("""
			    SELECT p FROM Product p
			    WHERE (:name IS NULL OR p.name ILIKE CONCAT(:name, '%'))
			      AND (:brand IS NULL OR p.brand = :brand)
			      AND (:categoryId IS NULL OR p.category.id = :categoryId)
			      AND (:subCategoryId IS NULL OR p.subcategory.id = :subCategoryId)
			      AND (:barcode IS NULL OR p.barcode = :barcode)
			      AND (:userId IS NULL OR p.createdBy.id = :userId)
			      AND (:startDate IS NULL OR p.createdAt >= :startDate)
			      AND (:endDate IS NULL OR p.createdAt <= :endDate)
			""")
	Page<Product> searchProducts(@Param("name") String name, @Param("brand") String brand,
			@Param("categoryId") UUID categoryId, @Param("subCategoryId") UUID subCategoryId,
			@Param("barcode") String barcode, @Param("userId") Integer userId, @Param("startDate") Instant startDate,
			@Param("endDate") Instant endDate, Pageable pageable);

	/*@Query("SELECT new net.foodeals.dto.ProductWithOfferDTO(p.image, p.name, p.newPrice, p.oldPrice, p.remainingStock) FROM Product p JOIN p.deals d WHERE d.subentity.id = :subentityId")
	List<ProductWithOfferDTO> getProductsWithPriceOffers(@Param("subentityId") UUID subentityId);*/

}
