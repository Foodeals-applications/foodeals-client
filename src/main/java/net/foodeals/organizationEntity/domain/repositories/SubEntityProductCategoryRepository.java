package net.foodeals.organizationEntity.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.organizationEntity.domain.entities.SubEntityProductCategory;
import net.foodeals.product.domain.entities.ProductCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubEntityProductCategoryRepository extends BaseRepository<SubEntityProductCategory
        , UUID> {

    @Query("SELECT sec FROM SubEntityProductCategory sec " +
            "JOIN sec.subEntityDomain sd " +
            "WHERE sec.subEntityDomain.id = :domainId")
    List<SubEntityProductCategory> findBySubEntityDomainId(@Param("domainId") UUID domainId);
    
    List<SubEntityProductCategory> findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(String name, Pageable pageable);

    default SubEntityProductCategory findByName(String name) {
        return findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(name, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);
    }

}
