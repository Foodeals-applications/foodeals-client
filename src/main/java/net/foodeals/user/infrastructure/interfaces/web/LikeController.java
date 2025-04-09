package net.foodeals.user.infrastructure.interfaces.web;

import lombok.RequiredArgsConstructor;
import net.foodeals.user.application.services.LikeService;
import net.foodeals.user.application.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    private final UserService userService;
    /**
     * Endpoint pour ajouter une sous-entité aux favoris
     */
    @PostMapping
    public ResponseEntity<String> addLike(
            @RequestParam UUID subEntityId
    ) {
        Integer userId=userService.getConnectedUser().getId();
        likeService.addLike(subEntityId, userId);
        return ResponseEntity.ok("Sous-entité ajoutée aux favoris.");
    }

    /**
     * Endpoint pour supprimer une sous-entité des favoris
     */
    @DeleteMapping
    public ResponseEntity<String> removeLike(
            @RequestParam UUID subEntityId

    ) {
        Integer userId=userService.getConnectedUser().getId();
        likeService.removeLike(subEntityId, userId);
        return ResponseEntity.ok("Sous-entité supprimée des favoris.");
    }
}
