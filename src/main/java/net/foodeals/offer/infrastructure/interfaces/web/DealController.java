package net.foodeals.offer.infrastructure.interfaces.web;

import java.util.ArrayList;
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
import net.foodeals.offer.application.dtos.requests.DealDto;
import net.foodeals.offer.application.dtos.requests.DealProDto;
import net.foodeals.offer.application.dtos.requests.RelaunchDealDto;
import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.offer.application.dtos.responses.DealDetailsResponse;
import net.foodeals.offer.application.dtos.responses.DealProResponse;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.offer.application.services.DealService;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.application.dtos.requests.SupplementDto;
import net.foodeals.user.application.services.UserService;

@RestController
@RequestMapping("v1/deals")
@RequiredArgsConstructor
public class DealController {

	private final DealService service;
	private final UserService userService;
	private final ModelMapper mapper;

	@GetMapping
	public ResponseEntity<Page<DealResponse>> getAll(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		Page<DealResponse> response = null;

		response = service.findAll(pageNum, pageSize).map(deal -> mapper.map(deal, DealResponse.class));

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DealDetailsResponse> get(@PathVariable UUID id) {
		DealDetailsResponse response = null;

		response = mapper.map(service.findById(id), DealDetailsResponse.class);

		return ResponseEntity.ok(response);
	}

	@GetMapping("dealpro/{id}")
	public ResponseEntity<DealProResponse> getDealPro(@PathVariable UUID id) {
		DealProResponse response = null;

		response = mapper.map(service.findById(id), DealProResponse.class);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/dealpro")
	public ResponseEntity<Page<DealResponse>> getAllDealPro(Pageable pageable) {
		Page<DealResponse> response = service.findDealPro(pageable).map(deal -> mapper.map(deal, DealResponse.class));

		if (response.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/history")
	public ResponseEntity<Page<DealResponse>> getHistoricDeals(Pageable pageable) {
		Page<DealResponse> response = null;

		response = service.getExpiredAndUnavailableDeals(null).map(deal -> mapper.map(deal, DealResponse.class));

		return ResponseEntity.ok(response);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<DealResponse> create(@RequestPart("deal") @Valid DealDto dto,
			@RequestPart(value = "productImage", required = false) MultipartFile productImage,
			@RequestPart(value = "supplementImages", required = false) List<MultipartFile> supplementImages) {

		if (productImage != null) {
			String imagePath = service.saveFile(productImage);
			ProductRequest productRequest = new ProductRequest(dto.produtRequest().name(), dto.produtRequest().title(),
					dto.produtRequest().description(), dto.produtRequest().barcode(), dto.produtRequest().type(),
					dto.produtRequest().price(), imagePath, dto.produtRequest().categoryId(),
					dto.produtRequest().subCategoryId(), dto.produtRequest().brand(), dto.produtRequest().rayon(),
					dto.produtRequest().expirationDate());
			dto = new DealDto(productRequest, dto.productId(), dto.quantity(), dto.price(),
					dto.dealUnityType(),dto.supplements(),
					dto.supplementsIds(), dto.salePrice(), dto.reduction(), dto.modalityTypes(), dto.modalityPaiement(),
					dto.deliveryFee(), dto.openTimes(), dto.publishAs(), dto.category());
		}

		if (supplementImages != null && !supplementImages.isEmpty()) {
			List<SupplementDto> updatedSupplements = new ArrayList();
			for (int i = 0; i < supplementImages.size(); i++) {
				MultipartFile supplementImage = supplementImages.get(i);
				String supplementImagePath = service.saveFile(supplementImage);

				if (i < dto.supplements().size()) {
					SupplementDto supplement = dto.supplements().get(i);
					updatedSupplements
							.add(new SupplementDto(supplement.name(), supplement.price(), supplementImagePath));
				}
			}
			dto = new DealDto(dto.produtRequest(), dto.productId(), dto.quantity(), dto.price(),
					dto.dealUnityType(),updatedSupplements,
					dto.supplementsIds(), dto.salePrice(), dto.reduction(), dto.modalityTypes(), dto.modalityPaiement(),
					dto.deliveryFee(), dto.openTimes(), dto.publishAs(), dto.category());
		}

		final DealResponse response = mapper.map(service.create(dto), DealResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping(value = "/create-dealpro", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<DealResponse> createDealPro(@RequestPart("deal") DealProDto dto,
			@RequestPart(value = "photoDealProPath", required = true) MultipartFile photoDealProPath) {

		String photoPath = service.saveFile(photoDealProPath);

		final DealResponse response = mapper.map(service.createPro(dto, photoPath), DealResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<DealResponse> updateDeal(@PathVariable UUID id, @RequestPart("deal") @Valid DealDto dto,
			@RequestPart(value = "productImage", required = false) MultipartFile productImage,
			@RequestPart(value = "supplementImages", required = false) List<MultipartFile> supplementImages) {

		if (productImage != null) {
			String imagePath = service.saveFile(productImage);
			ProductRequest productRequest = new ProductRequest(dto.produtRequest().name(), dto.produtRequest().title(),
					dto.produtRequest().description(), dto.produtRequest().barcode(), dto.produtRequest().type(),
					dto.produtRequest().price(), imagePath, dto.produtRequest().categoryId(),
					dto.produtRequest().subCategoryId(), dto.produtRequest().brand(), dto.produtRequest().rayon(),
					dto.produtRequest().expirationDate());
			dto = new DealDto(productRequest, dto.productId(), dto.quantity(), dto.price(),
					dto.dealUnityType(),dto.supplements(),
					dto.supplementsIds(), dto.salePrice(), dto.reduction(), dto.modalityTypes(), dto.modalityPaiement(),
					dto.deliveryFee(), dto.openTimes(), dto.publishAs(), dto.category());
		}

		if (supplementImages != null && !supplementImages.isEmpty()) {
			List<SupplementDto> updatedSupplements = new ArrayList();
			for (int i = 0; i < supplementImages.size(); i++) {
				MultipartFile supplementImage = supplementImages.get(i);
				String supplementImagePath = service.saveFile(supplementImage);

				if (i < dto.supplements().size()) {
					SupplementDto supplement = dto.supplements().get(i);
					updatedSupplements
							.add(new SupplementDto(supplement.name(), supplement.price(), supplementImagePath));
				}
			}
			dto = new DealDto(dto.produtRequest(), dto.productId(), dto.quantity(), dto.price(),
					dto.dealUnityType(),updatedSupplements,
					dto.supplementsIds(), dto.salePrice(), dto.reduction(), dto.modalityTypes(), dto.modalityPaiement(),
					dto.deliveryFee(), dto.openTimes(), dto.publishAs(), dto.category());
		}

		final DealResponse response = mapper.map(service.update(id, dto), DealResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/relaunch/{id}")
	public ResponseEntity<DealResponse> create(@RequestBody @Valid RelaunchDealDto dto, @PathVariable UUID id) {
		final DealResponse response = mapper.map(service.relaunchDeal(dto, id), DealResponse.class);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteDeal(@PathVariable UUID id, @RequestParam String reason,
			@RequestParam String motif) {
		service.deleteDeal(id, reason, motif);
		return ResponseEntity.noContent().build();
	}

	

}
