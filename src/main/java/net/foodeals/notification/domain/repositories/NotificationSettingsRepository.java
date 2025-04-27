package net.foodeals.notification.domain.repositories;

import net.foodeals.notification.domain.entity.NotificationSettings;
import net.foodeals.user.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, UUID> {
    Optional<NotificationSettings> findByUser(User user);
}