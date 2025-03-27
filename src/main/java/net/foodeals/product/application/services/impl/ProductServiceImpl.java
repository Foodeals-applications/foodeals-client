package net.foodeals.product.application.services.impl;

import static net.foodeals.common.Utils.SlugUtil.makeUniqueSlug;
import static net.foodeals.common.Utils.SlugUtil.toSlug;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.NotFoundException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.common.services.BarcodeService;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.product.application.services.ProductCategoryService;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.product.application.services.ProductSubCategoryService;
import net.foodeals.product.domain.entities.DeliveryMethod;
import net.foodeals.product.domain.entities.PaymentMethodProduct;
import net.foodeals.product.domain.entities.PickupCondition;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.entities.ProductSubCategory;
import net.foodeals.product.domain.exceptions.ProductNotFoundException;
import net.foodeals.product.domain.repositories.DeliveryMethodRepository;
import net.foodeals.product.domain.repositories.PaymentMethodRepository;
import net.foodeals.product.domain.repositories.PickupConditionRepository;
import net.foodeals.product.domain.repositories.ProductRepository;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

@Service
@Transactional
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService {

	private final ProductRepository repository;
	private final ProductCategoryService categoryService;
	private final ProductSubCategoryService subCategoryService;
	private final BarcodeService barcodeService;
	private final PaymentMethodRepository paymentMethodRepository;
	private final DeliveryMethodRepository deliveryMethodRepository;
	private final PickupConditionRepository pickupConditionRepository;
	private final UserService userService;
	private final ModelMapper mapper;

	@Value("${upload.directory}")
	private String uploadDir;

	@Override
	public List<Product> findAll() {
		return repository.findAll();
	}

	@Override
	public Page<Product> findAll(Integer pageNumber, Integer pageSize) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public Product findById(UUID id) {
		return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}

	@Override
	public Product create(ProductRequest request) {

		Product product = new Product();

		if (request.categoryId() != null) {
			final ProductCategory category = categoryService.findById(request.categoryId());
			product.setCategory(category);
		}

		if (request.subCategoryId() != null) {
			final ProductSubCategory subCategory = subCategoryService.findById(request.subCategoryId());
			product.setSubcategory(subCategory);
		}

		product.setName(request.name());
		product.setTitle(request.title());
		product.setDescription(request.description());
		product.setProductImagePath(request.productImagePath());
		product.setBarcode(request.barcode());
		product.setType(request.type());
		product.setPrice(request.price());
		product.setBrand(request.brand());
		product.setRayon(request.rayon());
		product.setSlug(makeUniqueSlug(toSlug(request.name()), repository));
		User createdBy = userService.getConnectedUser();
		product.setCreatedBy(createdBy);
		return repository.save(product);
	}

	@Override
	public Product update(UUID id, ProductRequest request) {
		final Product product = findById(id);

		final ProductCategory category = categoryService.findById(request.categoryId());
		final ProductSubCategory subCategory = subCategoryService.findById(request.subCategoryId());

		product.setName(request.name());
		product.setTitle(request.title());
		product.setDescription(request.description());
		product.setProductImagePath(request.productImagePath());
		product.setBarcode(request.barcode());
		product.setType(request.type());
		product.setPrice(request.price());
		product.setCategory(category);
		product.setSubcategory(subCategory);
		product.setBrand(request.brand());
		product.setRayon(request.rayon());
		product.setSlug(makeUniqueSlug(toSlug(request.name()), repository));
		return repository.save(product);
	}

	public Product updateProduct(UUID productId, UUID paymentMethodId, UUID deliveryMethodId,
			List<PickupCondition> newPickupConditions) {
		Product product = repository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

		PaymentMethodProduct paymentMethodProduct = paymentMethodRepository.findById(paymentMethodId)
				.orElseThrow(() -> new RuntimeException("Payment Method not found"));
		product.setPaymentMethodProduct(paymentMethodProduct);

		DeliveryMethod deliveryMethod = deliveryMethodRepository.findById(deliveryMethodId)
				.orElseThrow(() -> new RuntimeException("Delivery Method not found"));
		product.setDeliveryMethod(deliveryMethod);

		// Update Pickup Conditions
		pickupConditionRepository.deleteAll(product.getPickupConditions()); // Clear existing
		product.setPickupConditions(newPickupConditions); // Add new conditions
		product = repository.save(product);
		for (PickupCondition pickupCondition : newPickupConditions) {
			pickupCondition.setProduct(product);
			pickupConditionRepository.save(pickupCondition);
		}
		return repository.save(product);
	}

	@Override
	public void delete(UUID id) {
		if (!repository.existsById(id))
			throw new ProductNotFoundException(id);

		repository.softDelete(id);
	}

	@Override
	public Product getProductByBarCode(InputStream imageStream) throws ProductNotFoundException, NotFoundException {
		try {

			String barCode = barcodeService.readBarcode(imageStream).trim();
			return repository.findByBarcode(barCode).orElseThrow(() -> new ProductNotFoundException(barCode));
		} catch (IOException e) {

			System.out.println("Error reading barcode from image");
			throw new RuntimeException("Failed to read barcode from image", e);
		}
	}

	@Override
	public String saveFile(MultipartFile file) {

		Path path = Paths.get(uploadDir, file.getOriginalFilename());
		try {
			Files.createDirectories(path.getParent());
			Files.write(path, file.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Problem while saving the file.", e);
		}

		return file.getOriginalFilename();
	}

	@Override
	public void deleteProduct(UUID id, String reason, String motif) {

		Product product = findById(id);
		if (!Objects.isNull(product)) {
			product.setReason(reason);
			product.setMotif(motif);
			repository.save(product);
			repository.softDelete(id);
		} else {
			throw new ProductNotFoundException(id);
		}

	}

	@Override
	public Product applyDiscount(UUID productId, int discountPercentage) {
		Product product = repository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));

		if (discountPercentage < 0 || discountPercentage > 100) {
			throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
		}

		BigDecimal originalPrice = product.getPrice().amount();

		BigDecimal discount = new BigDecimal(discountPercentage).divide(new BigDecimal(100));

		BigDecimal discountedPrice = originalPrice.multiply(BigDecimal.ONE.subtract(discount));

		Currency currentCurrency = product.getPrice().currency();
		Price newPrice = new Price(discountedPrice, currentCurrency);
		product.setPrice(newPrice);
		repository.save(product);
		return product;
	}

	@Override
	public Page<Product> searchProducts(String name, String brand, UUID categoryId, UUID subCategoryId, String barcode,
			Integer userId, Instant startDate, Instant endDate, Pageable pageable) {

		return repository.searchProducts(name, brand, categoryId, subCategoryId, barcode, userId, startDate, endDate,
				pageable);
	}

	@Override
	public Page<Product> findProductsByName(String name, Pageable pageable) {
		return repository.findProductsByName(name, pageable);
	}
}