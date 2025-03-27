package net.foodeals.delivery.domain.repositories;

import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.delivery.domain.entities.DeliveryPosition;

public interface DeliveryPositionRepository extends BaseRepository<DeliveryPosition, UUID> {
}
