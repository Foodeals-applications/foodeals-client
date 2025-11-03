package net.foodeals.notification.application.services;

import net.foodeals.core.domain.entities.NotificationSettings;
import net.foodeals.core.domain.entities.User;
import net.foodeals.notification.application.dtos.responses.NotificationCountResponse;

public interface NotificationSettingsService {

    NotificationSettings getSettingsForUser(User user);


    NotificationSettings updateSettings(User user, NotificationSettings newSettings);


    NotificationSettings resetSettings(User user);

    public NotificationCountResponse getNotificationCounts();

    void sendReferralInvitation(String toEmail, String message, String senderName);
}
