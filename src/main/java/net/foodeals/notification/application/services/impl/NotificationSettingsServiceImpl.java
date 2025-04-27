package net.foodeals.notification.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.notification.application.services.NotificationSettingsService;
import net.foodeals.notification.domain.entity.NotificationSettings;
import net.foodeals.notification.domain.repositories.NotificationSettingsRepository;
import net.foodeals.user.domain.entities.User;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class NotificationSettingsServiceImpl implements NotificationSettingsService {


    private final NotificationSettingsRepository repository;

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
}
