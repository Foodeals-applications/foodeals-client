package net.foodeals.notification.application.dtos.responses;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.notification.domain.enums.NotificationStatus;
import net.foodeals.notification.domain.enums.TypeRequest;
import net.foodeals.user.application.dtos.responses.UserResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

	private UUID id;
	private String title;
	private String content;
	private String attachementPath;
	private UserResponse sender;
	private Date creationDate;
	private TypeRequest typeRequest;
	private NotificationStatus status ;
	private UUID subentityId;

}
