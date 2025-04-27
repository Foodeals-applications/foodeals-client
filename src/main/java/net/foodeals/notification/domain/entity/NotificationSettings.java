package net.foodeals.notification.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.user.domain.entities.User;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "notifications-settings")

@Getter
@Setter
public class NotificationSettings extends AbstractEntity<UUID> {


    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID Id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private boolean calendarReminders;
    private boolean pushNotifications;
    private boolean importantUpdates;
    private boolean promotions;
}
