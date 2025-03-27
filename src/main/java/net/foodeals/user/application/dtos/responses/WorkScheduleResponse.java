package net.foodeals.user.application.dtos.responses;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkScheduleResponse {

	private UUID id;

	private DayOfWeek dayOfWeek;

	private LocalTime morningStart;
	private LocalTime morningEnd;
	private LocalTime afternoonStart;
	private LocalTime afternoonEnd;

}
