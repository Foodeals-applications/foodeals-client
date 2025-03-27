package net.foodeals.organizationEntity.Controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.requests.SubEntityRequest;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityResponse;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.user.application.dtos.responses.UserResponse;

@RestController
@RequestMapping("v1/subentities")
@RequiredArgsConstructor
public class SubEntityController {

	private final SubEntityService service;
	private final ModelMapper mapper;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<SubEntityResponse> create(@RequestPart("subEntity") SubEntityRequest request,
			@RequestPart("profilImage") MultipartFile profilImage,
			@RequestPart("coverImage") MultipartFile coverImage) {

		String imageProfilPath = service.saveFile(profilImage);
		String coverProfilPath = service.saveFile(coverImage);

		request = new SubEntityRequest(request.name(), request.activiteNames(), imageProfilPath, coverProfilPath,
				request.email(), request.phone(), request.solutionNames(), request.managerId(), request.cityId(),
				request.regionId(), request.countryId(), request.exactAdresse(), request.iFrame());
		final SubEntityResponse response = mapper.map(service.create(request), SubEntityResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SubEntityResponse> getById(@PathVariable UUID id) {
		final SubEntityResponse response = mapper.map(service.findById(id), SubEntityResponse.class);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/confirm-subentity/{id}")
	public ResponseEntity<SubEntityResponse> confirmSubEntity(@PathVariable UUID id) {
		final SubEntityResponse response = mapper.map(service.confirmSubEntity(id),SubEntityResponse.class);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<Page<SubEntityResponse>> getAll(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		final Page<SubEntityResponse> response = service.findAll(pageNum, pageSize)
				.map(subEntity -> mapper.map(subEntity, SubEntityResponse.class));
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("all/{status}")
	public ResponseEntity<Page<SubEntityResponse>> getAllByStatus(@PageableDefault(size = 10, sort = "id") Pageable pageable,@PathVariable String status) {
		final Page<SubEntityResponse> response = service.getAllByStatus(status,pageable)
				.map(subEntity -> mapper.map(subEntity, SubEntityResponse.class));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/all")
	public ResponseEntity<List<SubEntityResponse>> getAll() {
		final List<SubEntityResponse> responses = service.findAll().stream()
				.map(subEntity -> mapper.map(subEntity, SubEntityResponse.class)).toList();
		return ResponseEntity.ok(responses);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestParam String reason, @RequestParam String motif) {
		service.deleteSubEntity(id, reason, motif);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<SubEntityResponse> update(@RequestPart("subEntity") SubEntityRequest request,
			@RequestPart(value = "profilImage", required = false) MultipartFile profilImage,
			@RequestPart(value = "coverImage", required = false) MultipartFile coverImage, @PathVariable UUID id) {
		String imageProfilPath = null;
		String coverProfilPath = null;
		if (profilImage != null) {
			imageProfilPath = service.saveFile(profilImage);
		} else {
			imageProfilPath = request.avatarPath();
		}
		if (coverImage != null) {
			coverProfilPath = service.saveFile(coverImage);
		} else {
			coverProfilPath = request.coverPath();
		}

		request = new SubEntityRequest(request.name(), request.activiteNames(), imageProfilPath, coverProfilPath,
				request.email(), request.phone(), request.solutionNames(), request.managerId(), request.cityId(),
				request.regionId(), request.countryId(), request.exactAdresse(), request.iFrame());
		final SubEntityResponse response = mapper.map(service.update(id, request), SubEntityResponse.class);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@GetMapping("/filter")
	public ResponseEntity<Page<SubEntityResponse>> filterSubEntities(
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
			@RequestParam(value = "raisonSociale", required = false) String raisonSociale,
			@RequestParam(value = "managerId", required = false) UUID managerId,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "cityId", required = false) UUID cityId,
			@RequestParam(value = "solutionId", required = false) UUID solutionId,
			@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		Page<SubEntityResponse> responses = null;

		responses = service.filterSubEntities(startDate, endDate, raisonSociale, managerId, email, phone, cityId,
				solutionId, pageable).map(subEntity -> mapper.map(subEntity, SubEntityResponse.class));

		if (!responses.hasContent()) {
			responses = service.findAll(pageable.getPageNumber(), pageable.getPageSize())
					.map(subEntity -> mapper.map(subEntity, SubEntityResponse.class));
		}

		return ResponseEntity.ok(responses);
	}

}
