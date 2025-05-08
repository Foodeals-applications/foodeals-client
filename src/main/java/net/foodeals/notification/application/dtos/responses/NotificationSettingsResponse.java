package net.foodeals.notification.application.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationSettingsResponse {

	private boolean calendarReminders;
    private boolean pushNotifications;
    private boolean importantUpdates;
    private boolean promotions;
    private boolean notificationBoxSurprise;
    private List<String> remindMe;

}
