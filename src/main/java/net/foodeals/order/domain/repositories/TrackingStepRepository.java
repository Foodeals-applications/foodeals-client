package net.foodeals.order.domain.repositories;

import net.foodeals.order.domain.entities.TrackingStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingStepRepository extends JpaRepository<TrackingStep,Long> {
}
