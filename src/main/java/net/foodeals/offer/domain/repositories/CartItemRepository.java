package net.foodeals.offer.domain.repositories;


import java.util.List;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.offer.domain.entities.CartItem;

public interface CartItemRepository extends BaseRepository<CartItem, UUID> {
    List<CartItem> findBySubEntityId(UUID storeId);
    List<CartItem> findAllByIdIn(List<UUID> ids);
}
