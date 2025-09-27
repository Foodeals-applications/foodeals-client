package net.foodeals.businessrecomendations.domain.repositories;

import net.foodeals.businessrecomendations.domain.entities.BusinessRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessRecommendationRepository extends JpaRepository<BusinessRecommendation, UUID> {}
