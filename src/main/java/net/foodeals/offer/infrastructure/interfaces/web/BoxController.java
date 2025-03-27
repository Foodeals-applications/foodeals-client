package net.foodeals.offer.infrastructure.interfaces.web;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.requests.BoxDto;
import net.foodeals.offer.application.dtos.requests.RelaunchDealDto;
import net.foodeals.offer.application.dtos.responses.BoxDetailsResponse;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.offer.application.dtos.responses.RelaunchModifyHistoryResponse;
import net.foodeals.offer.application.services.BoxService;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.ModifiyHistory;
import net.foodeals.offer.domain.entities.RelaunchHistory;
import net.foodeals.offer.domain.enums.BoxType;
import net.foodeals.offer.domain.repositories.ModifiyHistoryRepository;
import net.foodeals.offer.domain.repositories.RelaunchHistoryRepository;
import net.foodeals.offer.infrastructure.modelMapperConfig.BoxResponse;

@RestController
@RequestMapping("v1/boxs")
@RequiredArgsConstructor
public class BoxController {

	private final BoxService service;
	private final RelaunchHistoryRepository relaunchHistoryRepository;
	private final ModifiyHistoryRepository modifiyHistoryRepository;
	private final ModelMapper mapper;

	@GetMapping
	public ResponseEntity<Page<BoxResponse>> getAll(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		Page<BoxResponse> response = null;

		response = service.findAll(pageNum, pageSize).map(box -> mapper.map(box, BoxResponse.class));

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BoxDetailsResponse> getAll(@PathVariable UUID id) {
		BoxDetailsResponse response = null;

		response = mapper.map(service.findById(id), BoxDetailsResponse.class);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/box-normal/{id}")
	public ResponseEntity<BoxDetailsResponse> getBoxNormalById(@PathVariable UUID id) {

		BoxDetailsResponse response = null;
		response = mapper.map(service.findByIdAndType(id, BoxType.NORMAL_BOX), BoxDetailsResponse.class);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/box-surprise/{id}")
	public ResponseEntity<BoxDetailsResponse> getBoxSurpriseById(@PathVariable UUID id) {
		BoxDetailsResponse response = null;
		response = mapper.map(service.findByIdAndType(id, BoxType.MYSTERY_BOX), BoxDetailsResponse.class);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/all/normal")
	public ResponseEntity<Page<BoxResponse>> getAllNormalBox(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		Page<BoxResponse> response = null;

		response = service.findAllNormalBox(pageNum, pageSize).map(box -> mapper.map(box, BoxResponse.class));

		return ResponseEntity.ok(response);
	}

	@GetMapping("/all/surprise")
	public ResponseEntity<Page<BoxResponse>> getAllSurpriseBox(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		Page<BoxResponse> response = null;

		response = service.findAllSurpriseBox(pageNum, pageSize).map(box -> mapper.map(box, BoxResponse.class));

		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "box-normal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BoxResponse> createBoxNormal(@RequestPart("box") @Valid BoxDto dto,
			@RequestPart("photoBox") MultipartFile photoBox) {

		String imagePath = service.saveFile(photoBox);
		BoxDto updatedDto = new BoxDto(dto.title(), dto.description(), imagePath, dto.type(), dto.price(),
				dto.reduction(), dto.quantity(), dto.openTimes(), dto.modalityTypes(), dto.modalityPaiement(),
				dto.deliveryFee(), dto.publishAs(), dto.category());

		final BoxResponse response = mapper.map(service.createBox(updatedDto, BoxType.NORMAL_BOX), BoxResponse.class);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(value = "update/box-normal/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BoxResponse> updateBoxNormal(@PathVariable UUID id, @RequestPart("box") @Valid BoxDto dto,
			@RequestPart(value = "photoBox", required = false) MultipartFile photoBox) {

		String imagePath = null;
		if (photoBox != null) {
			imagePath = service.saveFile(photoBox);
		} else {
			imagePath = dto.photoBoxPath();
		}

		BoxDto updatedDto = new BoxDto(dto.title(), dto.description(), imagePath, dto.type(), dto.price(),
				dto.reduction(), dto.quantity(), dto.openTimes(), dto.modalityTypes(), dto.modalityPaiement(),
				dto.deliveryFee(), dto.publishAs(), dto.category());

		final BoxResponse response = mapper.map(service.updateBox(id, updatedDto, BoxType.NORMAL_BOX),
				BoxResponse.class);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "box-surprise", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BoxResponse> createBoxSurprise(@RequestPart("box") @Valid BoxDto dto) {

		final BoxResponse response = mapper.map(service.createBox(dto, BoxType.MYSTERY_BOX), BoxResponse.class);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(value = "update/box-surprise/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BoxResponse> updateBoxSurprise(@PathVariable UUID id, @RequestPart("box") @Valid BoxDto dto) {

		final BoxResponse response = mapper.map(service.updateBox(id, dto, BoxType.MYSTERY_BOX), BoxResponse.class);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteBox(@PathVariable UUID id, @RequestParam String reason,
			@RequestParam String motif) {
		service.deleteBox(id, reason, motif);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/history/box-normal")
	public ResponseEntity<Page<BoxResponse>> getHistoricBoxsNormals(Pageable pageable) {
		Page<BoxResponse> response = null;

		response = service.getExpiredAndUnavailableBoxs(null, BoxType.NORMAL_BOX)
				.map(box -> mapper.map(box, BoxResponse.class));

		return ResponseEntity.ok(response);
	}

	@GetMapping("/history/box-surprise")
	public ResponseEntity<Page<BoxResponse>> getHistoricBoxsSurprises(Pageable pageable) {
		Page<BoxResponse> response = null;

		response = service.getExpiredAndUnavailableBoxs(null, BoxType.MYSTERY_BOX)
				.map(box -> mapper.map(box, BoxResponse.class));

		return ResponseEntity.ok(response);
	}

	@PutMapping("/relaunch/{id}")
	public ResponseEntity<BoxResponse> relaunchBox(@PathVariable UUID id) {
		final BoxResponse response = mapper.map(service.relaunchBox(id), BoxResponse.class);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/relaunch-modifiy-history/{boxId}")
	public ResponseEntity<RelaunchModifyHistoryResponse> getRelaunchHistory(@PathVariable UUID boxId) {
		Box box =service.findById(boxId);
		RelaunchHistory history = relaunchHistoryRepository.findByBox(box).get(0);
		String organizationNameModify=null;
		LocalDateTime modifiyDate =null;
		ModifiyHistory modifiyHistory=modifiyHistoryRepository.findByBox(box).get(0);
		organizationNameModify=modifiyHistory.getOrganization().getName();
		modifiyDate=modifiyHistory.getModifyDate();
		RelaunchModifyHistoryResponse response = new RelaunchModifyHistoryResponse(history.getOrganization().getName(), 
				history.getRelaunchDate(),organizationNameModify,modifiyDate);

		return ResponseEntity.ok(response);
	}

}
