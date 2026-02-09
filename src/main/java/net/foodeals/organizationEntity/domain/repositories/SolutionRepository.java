package net.foodeals.organizationEntity.domain.repositories;

import net.foodeals.organizationEntity.domain.entities.Solution;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SolutionRepository extends JpaRepository<Solution, UUID> {
    Set<Solution> findByNameIn(List<String> solutionsNames);
    List<Solution> findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(String name, Pageable pageable);

    default Solution findByName(String name) {
        return findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(name, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);
    }
}
