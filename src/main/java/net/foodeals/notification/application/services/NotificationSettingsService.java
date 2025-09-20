package net.foodeals.notification.application.services;

import net.foodeals.notification.application.dtos.responses.NotificationCountResponse;
import net.foodeals.notification.domain.entity.NotificationSettings;
import net.foodeals.user.domain.entities.User;

public interface NotificationSettingsService {

    NotificationSettings getSettingsForUser(User user);


    NotificationSettings updateSettings(User user, NotificationSettings newSettings);


    NotificationSettings resetSettings(User user);

    public NotificationCountResponse getNotificationCounts();
}
