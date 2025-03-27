package net.foodeals.offer.domain.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import net.foodeals.offer.domain.entities.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserId(Integer userId);
}

