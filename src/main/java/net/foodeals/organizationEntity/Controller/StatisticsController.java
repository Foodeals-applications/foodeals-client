package net.foodeals.organizationEntity.Controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.responses.StatisticsResponse;
import net.foodeals.organizationEntity.application.services.StatisticsService;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.user.application.services.UserService;

@RestController
@RequestMapping("v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

	private final StatisticsService statisticsService;

	private final UserService userService;

	@GetMapping
	public ResponseEntity<StatisticsResponse> getStatistics(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		OrganizationEntity organizationEntity = userService.getConnectedUser().getOrganizationEntity();
		StatisticsResponse response = statisticsService.getStatistics(organizationEntity, startDate, endDate);
		return ResponseEntity.ok(response);
	}

}
