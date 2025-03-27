package net.foodeals.offer.domain.repositories;


import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.offer.domain.entities.CartItem;

public interface CartItemRepository extends BaseRepository<CartItem, UUID> {
}
