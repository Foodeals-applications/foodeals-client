package net.foodeals.dlc.infrastructure.interfaces.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.services.ExcelService;
import net.foodeals.dlc.application.dtos.requests.DlcRequest;
import net.foodeals.dlc.application.dtos.responses.DlcDetailsResponse;
import net.foodeals.dlc.application.dtos.responses.DlcResponse;
import net.foodeals.dlc.application.dtos.responses.ModificationDetailsResponse;
import net.foodeals.dlc.application.services.DlcService;
import net.foodeals.dlc.application.services.ModificationService;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.product.domain.exceptions.ProductNotFoundException;

@RestController
@RequestMapping("v1/dlcs")
@RequiredArgsConstructor
public class DlcController {

	private final DlcService service;
	private final ProductService productService;
	private final ExcelService excelService;
	private final ModificationService modificationService;
	private final ModelMapper mapper;

	@GetMapping
	public ResponseEntity<Page<DlcResponse>> getAll(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		final Page<DlcResponse> response = service.findAll(pageNum, pageSize)
				.map(dlc -> mapper.map(dlc, DlcResponse.class));
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<DlcResponse> create(@RequestBody @Valid DlcRequest request) {
		final DlcResponse response = mapper.map(service.create(request), DlcResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DlcResponse> update(@RequestBody @Valid DlcRequest request, @PathVariable UUID id) {
		final DlcResponse response = mapper.map(service.update(id, request), DlcResponse.class);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadProducts(@RequestParam("file") MultipartFile file) throws IOException {
		excelService.readDlcsFromExcel(file);
		return ResponseEntity.ok("Dlcs uploaded successfully");
	}

	@PostMapping("/scan")
	public ResponseEntity<?> getProductByBarcode(@RequestParam("file") MultipartFile file)  {
		try (InputStream inputStream = file.getInputStream()) {
			ProductResponse response;
			try {
				response = mapper.map(productService.getProductByBarCode(inputStream), ProductResponse.class);
			} catch (ProductNotFoundException | NotFoundException e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found for barcode.");
			}
			return ResponseEntity.ok(response);
		} catch (IOException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file.");
		}
	}

	@PatchMapping("/{id}")
	public ResponseEntity<DlcResponse> applyDiscount(@PathVariable UUID id, @RequestParam int discountPercentage) {
		DlcResponse dlcResponse = mapper.map(service.applyDiscount(id, discountPercentage), DlcResponse.class);
		return ResponseEntity.ok(dlcResponse);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DlcDetailsResponse> findById(@PathVariable UUID id) {
		final DlcDetailsResponse response = service.findDlcDetails(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/last-modification/{userId}/{dlcId}")
	public ResponseEntity<ModificationDetailsResponse> getUserModificationDetails(@PathVariable UUID dlcId,
			@PathVariable Integer userId) {
		ModificationDetailsResponse details = modificationService.getUserModificationDetails(dlcId, userId);
		return ResponseEntity.ok(details);
	}
	
}
