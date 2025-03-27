package net.foodeals.notification.infrastructure.interfaces.web;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import net.foodeals.notification.application.dtos.requests.NotificationRequest;
import net.foodeals.notification.application.dtos.responses.NotificationResponse;
import net.foodeals.notification.application.services.NotificationService;
import net.foodeals.notification.domain.enums.NotificationStatus;

@RestController
@RequestMapping("v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService service;

	private final ModelMapper mapper;

	@GetMapping
	public ResponseEntity<Page<NotificationResponse>> getAll(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		final Page<NotificationResponse> response = service.findAll(pageNum, pageSize)
				.map(notification -> mapper.map(notification, NotificationResponse.class));
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/status/{status}")
	public ResponseEntity<Page<NotificationResponse>> getAllByStatus(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize,@PathVariable String status) {
		
		NotificationStatus notificationStatus=NotificationStatus.valueOf(status);
		PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
		final Page<NotificationResponse> response = service.findAllByStatus(notificationStatus, pageRequest)
				.map(notification -> mapper.map(notification, NotificationResponse.class));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NotificationResponse> get(@PathVariable UUID id) {
		final NotificationResponse response = mapper.map(service.findById(id), NotificationResponse.class);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<NotificationResponse> create(@RequestPart("request") NotificationRequest request,
			@RequestPart("file") MultipartFile file) {
		String filePath = service.saveFile(file);
		final NotificationResponse response = mapper.map(service.create(request, filePath), NotificationResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

}
