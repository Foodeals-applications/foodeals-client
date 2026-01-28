package net.foodeals.location.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.location.domain.entities.State;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface StateRepository extends BaseRepository<State, UUID> {
    List<State> findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(String name, Pageable pageable);

    default State findByName(String name) {
        return findByNameAndDeletedAtIsNullOrderByCreatedAtDesc(name, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);
    }

    State findByCode(String number);
}
