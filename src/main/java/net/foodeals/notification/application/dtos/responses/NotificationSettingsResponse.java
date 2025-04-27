package net.foodeals.notification.application.dtos.responses;

import java.util.Date;
import java.util.UUID;

import lombok.*;
import net.foodeals.notification.domain.enums.NotificationStatus;
import net.foodeals.notification.domain.enums.TypeRequest;
import net.foodeals.user.application.dtos.responses.UserResponse;

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

}
