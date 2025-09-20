package net.foodeals.notification.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.notification.domain.entity.Notification;
import net.foodeals.notification.domain.enums.NotificationStatus;
import net.foodeals.notification.domain.enums.TypeRequest;
import net.foodeals.user.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends BaseRepository<Notification, UUID> {

    Page<Notification> findAllByStatus(NotificationStatus status, Pageable pageable);

    List<Notification> findByUser(User user);

    long countByUserAndTypeRequest(User user, TypeRequest typeRequest);
}
