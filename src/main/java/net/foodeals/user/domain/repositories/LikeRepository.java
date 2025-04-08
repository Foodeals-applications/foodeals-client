package net.foodeals.user.domain.repositories;

import net.foodeals.user.domain.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {


    // Vérification : un utilisateur a-t-il déjà ajouté une sous-entité en favoris ?
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
            "FROM Like l WHERE l.subEntityId = :subEntityId AND l.userId = :userId")
    boolean existsBySubEntityIdAndUserId(@Param("subEntityId") UUID subEntityId, @Param("userId") Integer userId);

    // Supprime un favori pour une sous-entité et un utilisateur
    void deleteBySubEntityIdAndUserId(UUID subEntityId, Integer userId);

}
