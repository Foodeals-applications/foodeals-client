package net.foodeals.notification.domain.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.notification.domain.entity.Notification;
import net.foodeals.notification.domain.enums.NotificationStatus;

public interface NotificationRepository extends BaseRepository<Notification, UUID> {

	Page<Notification> findAllByStatus(NotificationStatus status, Pageable pageable);
}
