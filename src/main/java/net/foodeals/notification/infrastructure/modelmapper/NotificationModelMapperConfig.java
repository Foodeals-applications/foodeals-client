package net.foodeals.notification.infrastructure.modelmapper;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.notification.application.dtos.responses.NotificationResponse;
import net.foodeals.notification.domain.entity.Notification;
import net.foodeals.user.application.dtos.responses.UserResponse;

@Configuration
@RequiredArgsConstructor
@Transactional
public class NotificationModelMapperConfig {

	private final ModelMapper mapper;

	@PostConstruct
	public void configure() {
		mapper.addConverter(context -> {
			Notification notification = context.getSource();
			UserResponse user = mapper.map(notification.getUser(), UserResponse.class);
			Date creationDate = notification.getCreatedAt() != null ? Date.from(notification.getCreatedAt()) : null;
			return new NotificationResponse(notification.getId(), notification.getTitle(), notification.getContent(),notification.getAttachmentPath(),
					user, creationDate, notification.getTypeRequest(),notification.getStatus(),notification.getSubEntity().getId());

		}, Notification.class, NotificationResponse.class);

	}

}
