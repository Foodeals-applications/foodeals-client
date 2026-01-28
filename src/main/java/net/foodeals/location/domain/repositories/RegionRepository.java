package net.foodeals.location.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.location.domain.entities.Region;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface RegionRepository extends BaseRepository<Region, UUID> {
    List<Region> findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(String name, Pageable pageable);

    default Region findByName(String name) {
        return findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(name, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);
    }
}
