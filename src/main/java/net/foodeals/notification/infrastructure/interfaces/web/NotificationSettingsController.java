package net.foodeals.notification.infrastructure.interfaces.web;


import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.NotificationSettings;
import net.foodeals.core.domain.entities.User;
import net.foodeals.notification.application.dtos.responses.NotificationCountResponse;
import net.foodeals.notification.application.dtos.responses.NotificationSettingsResponse;
import net.foodeals.notification.application.services.NotificationSettingsService;
import net.foodeals.user.application.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/notifications-settings")
@RequiredArgsConstructor
public class NotificationSettingsController {

    private final NotificationSettingsService service;
    private final UserService userService;


    @GetMapping("/counts")
    public NotificationCountResponse getCounts() {
        return service.getNotificationCounts();
    }

    @GetMapping
    public ResponseEntity<NotificationSettingsResponse> getNotificationSettings() {
        User user = userService.getConnectedUser();
        NotificationSettings notificationSettings = service.getSettingsForUser(user);
        NotificationSettingsResponse notificationSettingsResponse = new NotificationSettingsResponse(notificationSettings.isCalendarReminders(),
                notificationSettings.isPushNotifications(), notificationSettings.isImportantUpdates(), notificationSettings.isPromotions(),
                notificationSettings.isNotificationBoxSurprise(),notificationSettings.getRemindMe());
        return ResponseEntity.ok(notificationSettingsResponse);
    }

    @PutMapping
    public ResponseEntity<NotificationSettingsResponse> updateNotificationSettings(
            @RequestBody NotificationSettings newSettings) {
        User user = userService.getConnectedUser();
        NotificationSettings notificationSettings = service.updateSettings(user, newSettings);
        NotificationSettingsResponse notificationSettingsResponse = new NotificationSettingsResponse(notificationSettings.isCalendarReminders(),
                notificationSettings.isPushNotifications(), notificationSettings.isImportantUpdates(), notificationSettings.isPromotions(),notificationSettings.isNotificationBoxSurprise(),
                notificationSettings.getRemindMe());
        return ResponseEntity.ok(notificationSettingsResponse);
    }


    @PutMapping("/reset")
    public ResponseEntity<NotificationSettingsResponse> resetNotificationSettings() {
        User user = userService.getConnectedUser();
        NotificationSettings notificationSettings = service.resetSettings(user);
        NotificationSettingsResponse notificationSettingsResponse = new NotificationSettingsResponse(notificationSettings.isCalendarReminders(),
                notificationSettings.isPushNotifications(), notificationSettings.isImportantUpdates(), notificationSettings.isPromotions(),notificationSettings.isNotificationBoxSurprise(),
                notificationSettings.getRemindMe());
        return ResponseEntity.ok(notificationSettingsResponse);
    }
}
