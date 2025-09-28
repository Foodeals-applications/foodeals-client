package net.foodeals.businessrecomendations.domain.repositories;

import net.foodeals.businessrecomendations.domain.entities.BusinessApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessApplicationRepository extends JpaRepository<BusinessApplication, UUID> {
}
