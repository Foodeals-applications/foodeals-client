package net.foodeals.organizationEntity.Controller;

import java.util.List;
import java.util.UUID;

import net.foodeals.core.domain.entities.Activity;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.requests.ActivityRequest;
import net.foodeals.organizationEntity.application.dtos.responses.ActivityResponseDto;
import net.foodeals.organizationEntity.application.services.ActivityService;

@RestController
@RequestMapping("v1/activities")
@RequiredArgsConstructor
public class ActivityController {

	private final ActivityService activityService;
	private final ModelMapper modelMapper;

	@GetMapping
	public ResponseEntity<List<ActivityResponseDto>> getAllActivities() {
		List<Activity> activities = this.activityService.findAll();
		List<ActivityResponseDto> activityResponses = activities.stream()
				.map(activity -> this.modelMapper.map(activity, ActivityResponseDto.class)).toList();
		return new ResponseEntity<List<ActivityResponseDto>>(activityResponses, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ActivityResponseDto> getActivityById(@PathVariable("id") UUID id) {
		Activity activity = this.activityService.findById(id);
		ActivityResponseDto activityResponse = this.modelMapper.map(activity, ActivityResponseDto.class);
		return new ResponseEntity<ActivityResponseDto>(activityResponse, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ActivityResponseDto> createAnActivity(@RequestBody ActivityRequest activityRequest) {
		Activity activity = this.activityService.create(activityRequest);
		ActivityResponseDto activityResponse = this.modelMapper.map(activity, ActivityResponseDto.class);
		return new ResponseEntity<ActivityResponseDto>(activityResponse, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ActivityResponseDto> updateAnActivity(@RequestBody ActivityRequest activityRequest,
			@PathVariable("id") UUID id) {
		Activity activity = this.activityService.update(id, activityRequest);
		ActivityResponseDto activityResponse = this.modelMapper.map(activity, ActivityResponseDto.class);
		return new ResponseEntity<ActivityResponseDto>(activityResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAnActivity(@PathVariable("id") UUID id) {
		this.activityService.delete(id);
		return new ResponseEntity<String>("Activity has been deleted", HttpStatus.OK);
	}
}
