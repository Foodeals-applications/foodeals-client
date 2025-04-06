package net.foodeals.user.domain.repositories;

import net.foodeals.user.domain.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {

    boolean existsBySubEntityIdAndUserId(UUID subEntityId, Integer userId);
}
