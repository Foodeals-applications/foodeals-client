package net.foodeals.organizationEntity.domain.repositories;

import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    Set<Activity> findByNameIn(List<String> activitiesNames);
    List<Activity> findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(String name, Pageable pageable);

    default Activity findByName(String name) {
        return findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(name, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);
    }
	List<Activity> findByOrganizationEntitiesContains(OrganizationEntity organizationEntity);
}
