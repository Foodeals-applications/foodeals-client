package net.foodeals.notification.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.NotificationSettings;
import net.foodeals.core.domain.entities.User;
import net.foodeals.core.domain.enums.TypeRequest;
import net.foodeals.core.repositories.NotificationRepository;
import net.foodeals.core.repositories.NotificationSettingsRepository;
import net.foodeals.notification.application.dtos.responses.NotificationCountResponse;
import net.foodeals.notification.application.services.NotificationSettingsService;

import net.foodeals.user.application.services.UserService;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class NotificationSettingsServiceImpl implements NotificationSettingsService {


    private final NotificationSettingsRepository repository;
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Override
    public NotificationSettings getSettingsForUser(User user) {
        return repository.findByUser(user)
                .orElseGet(() -> {
                    NotificationSettings settings = new NotificationSettings();
                    settings.setUser(user);
                    return repository.save(settings);
                });
    }

    @Override
    public NotificationSettings updateSettings(User user, NotificationSettings newSettings) {
        NotificationSettings existing = getSettingsForUser(user);
        existing.setCalendarReminders(newSettings.isCalendarReminders());
        existing.setPushNotifications(newSettings.isPushNotifications());
        existing.setImportantUpdates(newSettings.isImportantUpdates());
        existing.setPromotions(newSettings.isPromotions());
        existing.setNotificationBoxSurprise(newSettings.isNotificationBoxSurprise());
        existing.setRemindMe(newSettings.getRemindMe());
        return repository.save(existing);
    }

    @Override
    public NotificationSettings resetSettings(User user) {
        NotificationSettings settings = getSettingsForUser(user);
        settings.setCalendarReminders(false);
        settings.setPushNotifications(false);
        settings.setImportantUpdates(false);
        settings.setPromotions(false);
        return repository.save(settings);
    }


    public NotificationCountResponse getNotificationCounts() {
        User user = userService.getConnectedUser();

        long donations = notificationRepository.countByUserAndTypeRequest(user, TypeRequest.DONATION);
        long favorites = notificationRepository.countByUserAndTypeRequest(user, TypeRequest.FAVORITE);
        long coupons = notificationRepository.countByUserAndTypeRequest(user, TypeRequest.COUPON);

        return new NotificationCountResponse(donations, favorites, coupons);
    }

    @Override
    public void sendReferralInvitation(String toEmail, String message, String senderName) {
        // Placeholder: remplacer par envoi réel (Mail/SMS/Push)
        System.out.println("Sending referral invitation to " + toEmail + " from " + senderName + " message: " + message);
    }
}