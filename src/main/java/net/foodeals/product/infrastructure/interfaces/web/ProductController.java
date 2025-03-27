package net.foodeals.product.infrastructure.interfaces.web;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.common.services.ExcelService;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.product.domain.entities.DeliveryMethod;
import net.foodeals.product.domain.entities.PaymentMethodProduct;
import net.foodeals.product.domain.entities.PickupCondition;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.exceptions.ProductNotFoundException;
import net.foodeals.product.domain.repositories.DeliveryMethodRepository;
import net.foodeals.product.domain.repositories.PaymentMethodRepository;
import net.foodeals.product.domain.repositories.PickupConditionRepository;
import net.foodeals.product.domain.repositories.ProductRepository;
import net.foodeals.user.application.dtos.responses.UserResponse;

@RestController
@RequestMapping("v1/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;
	private final ExcelService excelService;
	private final ProductRepository productRepository;
	private final PaymentMethodRepository paymentMethodRepository;
	private final DeliveryMethodRepository deliveryMethodRepository;
	private final PickupConditionRepository pickupConditionRepository;
	private final ModelMapper mapper;

	@GetMapping
	public ResponseEntity<Page<ProductResponse>> getAll
	(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize,@RequestParam(value="name",required = false)String name) {
		Page<ProductResponse> response =null;
		if(name==null) {
		response = service.findAll(pageNum, pageSize)
				.map(product -> mapper.map(product, ProductResponse.class));
		}
		else {
			response=service.findProductsByName(name, PageRequest.of(pageNum, pageSize))
					.map(product -> mapper.map(product, ProductResponse.class));
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
		final ProductResponse response = mapper.map(service.findById(id), ProductResponse.class);
		return ResponseEntity.ok(response);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductResponse> create(@RequestPart("product") @Valid ProductRequest request,
			@RequestPart("productImage") MultipartFile productImage) {

		String imagePath = service.saveFile(productImage);

		request = new ProductRequest(request.name(), request.title(), request.description(), request.barcode(),
				request.type(), request.price(), imagePath, request.categoryId(), request.subCategoryId(),
				request.brand(), request.rayon(),null);

		final ProductResponse response = mapper.map(service.create(request), ProductResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID id, @RequestParam UUID paymentMethodId,
			@RequestParam UUID deliveryMethodId, @RequestBody List<PickupCondition> newPickupConditions) {

		Product product = service.findById(id);

		PaymentMethodProduct paymentMethodProduct = paymentMethodRepository.findById(paymentMethodId)
				.orElseThrow(() -> new RuntimeException("Payment Method not found"));
		product.setPaymentMethodProduct(paymentMethodProduct);

		DeliveryMethod deliveryMethod = deliveryMethodRepository.findById(deliveryMethodId)
				.orElseThrow(() -> new RuntimeException("Delivery Method not found"));
		product.setDeliveryMethod(deliveryMethod);

		pickupConditionRepository.deleteAll(product.getPickupConditions());

		Product updatedProduct = productRepository.save(product);

		List<PickupCondition> pickupConditionsProduct = new ArrayList<>();
		for (PickupCondition pickupCondition : newPickupConditions) {
			pickupCondition.setProduct(updatedProduct);
			pickupCondition = pickupConditionRepository.save(pickupCondition);
			pickupConditionsProduct.add(pickupCondition);
		}
		updatedProduct.setPickupConditions(pickupConditionsProduct);
		updatedProduct = productRepository.save(updatedProduct);
		final ProductResponse response = mapper.map(updatedProduct, ProductResponse.class);
		return ResponseEntity.ok(response);
	}

	@PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductResponse> update(@PathVariable UUID id,
			@RequestPart("product") @Valid ProductRequest request,
			@RequestPart(value = "productImage", required = false) MultipartFile productImage) {

		String imagePath = null;
		if (productImage != null) {
			imagePath = service.saveFile(productImage);
		} else {
			imagePath = request.productImagePath();
		}
		request = new ProductRequest(request.name(), request.title(), request.description(), request.barcode(),
				request.type(), request.price(), imagePath, request.categoryId(), request.subCategoryId(),
				request.brand(), request.rayon(),null);

		final ProductResponse response = mapper.map(service.update(id, request), ProductResponse.class);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable UUID id, @RequestParam String reason,
			@RequestParam String motif) {
		service.deleteProduct(id, reason, motif);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadProducts(@RequestParam("file") MultipartFile file) {
		try {
			excelService.readProductsFromExcel(file);

			excelService.readProductsFromExcel(file);

			return ResponseEntity.ok("Products uploaded successfully");
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Failed to upload products");
		}
	}

	@PostMapping("/scan")
	public ResponseEntity<?> getProductByBarcode(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty.");
		}

		try (InputStream inputStream = file.getInputStream()) {
			ProductResponse productResponse = mapper.map(service.getProductByBarCode(inputStream),
					ProductResponse.class);

			return ResponseEntity.ok(productResponse);

		} catch (ProductNotFoundException | NotFoundException e) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found for barcode.");

		} catch (IOException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file.");

		} catch (RuntimeException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read barcode from image.");
		}
	}

	@GetMapping("/search")
	public ResponseEntity<Page<ProductResponse>> searchProducts(@RequestParam(required = false) String name,
			@RequestParam(required = false) String brand, @RequestParam(required = false) UUID categoryId,
			@RequestParam(required = false) UUID subCategoryId, @RequestParam(required = false) String barcode,
			@RequestParam(required = false) Integer userId,
			@RequestParam(required = false) Instant startDate,
	        @RequestParam(required = false) Instant endDate,
			Pageable pageable) {
		Page<ProductResponse> products = service
				.searchProducts(name, brand, categoryId, subCategoryId, barcode,userId,startDate,endDate, pageable)
				.map(product -> mapper.map(product, ProductResponse.class));
		
		if (!products.hasContent()) {
			products= service.findAll(pageable.getPageNumber(), pageable.getPageSize())
					.map(product -> mapper.map(product, ProductResponse.class));
		}
		return ResponseEntity.ok(products);
	}

}