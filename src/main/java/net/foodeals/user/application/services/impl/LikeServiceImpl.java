package net.foodeals.user.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.foodeals.core.domain.entities.Like;
import net.foodeals.core.repositories.LikeRepository;
import net.foodeals.user.application.services.LikeService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Transactional
@Service
public class LikeServiceImpl implements LikeService {


    private final LikeRepository likeRepository;

    /**
     * Ajout d'un favori (like) pour une sous-entité
     *
     * @param subEntityId L'ID de la sous-entité à ajouter en favoris
     * @param userId      L'ID de l'utilisateur ajoutant la sous-entité en favoris
     */
    @Override
    public void addLike(UUID subEntityId, Integer userId) {
        // Vérifie si cette sous-entité est déjà dans les favoris de l'utilisateur
        if (!likeRepository.existsBySubEntityIdAndUserId(subEntityId, userId)) {
            // Si ce n'est pas déjà un favori, ajoute-le
            Like like = Like.builder()
                    .subEntityId(subEntityId)
                    .userId(userId)
                    .build();
            likeRepository.save(like);
        } else {
            throw new IllegalArgumentException("Cette sous-entité est déjà en favoris.");
        }
    }

    /**
     * Suppression d'un favori pour une sous-entité
     *
     * @param subEntityId L'ID de la sous-entité à supprimer des favoris
     * @param userId      L'ID de l'utilisateur
     */
    @Override
    public void removeLike(UUID subEntityId, Integer userId) {
        // Vérification et suppression
        if (likeRepository.existsBySubEntityIdAndUserId(subEntityId, userId)) {
            likeRepository.deleteBySubEntityIdAndUserId(subEntityId, userId);
        } else {
            throw new IllegalArgumentException("Cette sous-entité n'est pas dans vos favoris.");
        }
    }

}
