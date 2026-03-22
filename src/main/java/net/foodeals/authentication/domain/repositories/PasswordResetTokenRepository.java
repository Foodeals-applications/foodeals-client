package net.foodeals.authentication.domain.repositories;

import net.foodeals.authentication.domain.entities.PasswordResetToken;
import net.foodeals.common.contracts.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Query("UPDATE PasswordResetToken t SET t.usedAt = :usedAt WHERE t.user.id = :userId AND t.usedAt IS NULL")
    int markAllUsedForUser(@Param("userId") Integer userId, @Param("usedAt") Instant usedAt);
}
