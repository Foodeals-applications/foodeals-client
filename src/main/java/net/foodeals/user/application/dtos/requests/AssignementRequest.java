package net.foodeals.user.application.dtos.requests;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import net.foodeals.user.domain.entities.WorkSchedule;

public record AssignementRequest(
		@NotNull List<WorkSchedule>workSchedules)
 {
}