package net.foodeals.product.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.Review;

import java.util.UUID;

public interface ReviewRepository  extends BaseRepository<Review, UUID>{
}
