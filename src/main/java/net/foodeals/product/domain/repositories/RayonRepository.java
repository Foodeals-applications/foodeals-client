package net.foodeals.product.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.product.domain.entities.Rayon;

public interface RayonRepository extends BaseRepository<Rayon, UUID> {
	
	@Query("SELECT r FROM Rayon r WHERE r.name = :name AND r.deletedAt IS NULL")
	public Optional<Rayon>  findByName(String name);
}
