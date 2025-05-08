package net.foodeals.notification.domain.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(name = "notifications-settings")

@Getter
@Setter
public class NotificationSettings extends AbstractEntity<UUID> {


	    @Id
	    @GeneratedValue
	    @UuidGenerator
	    private UUID id;

	    @ManyToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

	    private boolean calendarReminders;
	    private boolean pushNotifications;
	    private boolean importantUpdates;
	    private boolean promotions;
	    private boolean notificationBoxSurprise;

	    @ElementCollection(fetch = FetchType.EAGER)
	    @CollectionTable(name = "notification_reminder_days", joinColumns = @JoinColumn(name = "notification_settings_id"))
	    @Column(name = "day")
	    private List<String> remindMe;
}
