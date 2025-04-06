package net.foodeals.organizationEntity.domain.repositories;


import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.organizationEntity.domain.entities.SubEntityDomain;
import net.foodeals.product.domain.entities.ProductCategory;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SubEntityDomainRepository extends BaseRepository<SubEntityDomain, UUID> {
    @Query("SELECT s FROM SubEntityDomain s WHERE s.name = :name AND s.deletedAt IS NULL")
    public Optional<SubEntityDomain> findByName(String name);




}
