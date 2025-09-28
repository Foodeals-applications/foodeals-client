package net.foodeals.order.domain.repositories;

import net.foodeals.delivery.domain.entities.DeliveryOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryOptionRepository extends JpaRepository<DeliveryOption, UUID> {
}
