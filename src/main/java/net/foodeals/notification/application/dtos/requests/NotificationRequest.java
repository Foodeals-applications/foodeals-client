package net.foodeals.notification.application.dtos.requests;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import net.foodeals.notification.domain.enums.TypeRequest;

public record NotificationRequest(@NotNull TypeRequest typeRequest, UUID subEntityId,
		@NotNull String title,@NotNull String content) {

}
