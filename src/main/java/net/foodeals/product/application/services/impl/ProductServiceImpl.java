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
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.product.application.dtos.responses.PriceResponse;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.domain.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.requests.ProductRequest;
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
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	private final ProductCategoryRepository categoryRepository;

	private final DealRepository dealRepository;


	public ProductResponse getProductDetails(UUID productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("Produit non trouvé"));

		// Récupérer les catégories liées
		List<String> categories = categoryRepository.findByProduct(productId)
				.stream()
				.map(ProductCategory::getName)
				.collect(Collectors.toList());
        Deal deal=dealRepository.findActiveDealByProduct(product.getId()).get();
		return ProductResponse.builder()
				.id(product.getId())
				.image(product.getProductImagePath())
				.name(product.getName())
				.description(product.getDescription())
				.price(new PriceResponse(deal.getOffer().getSalePrice().amount().doubleValue(),
						deal.getOffer().getPrice().amount().doubleValue()))
				.categories(categories)
				.stock(00)
				.subEntityId(product.getSubEntity().getId())
				.build();
	}

	@Override
	public List<Product> findAll() {
		return List.of();
	}

	@Override
	public Page<Product> findAll(Integer pageNumber, Integer pageSize) {
		return null;
	}

	@Override
	public Product findById(UUID uuid) {
		return null;
	}

	@Override
	public Product create(ProductRequest dto) {
		return null;
	}

	@Override
	public Product update(UUID uuid, ProductRequest dto) {
		return null;
	}

	@Override
	public void delete(UUID uuid) {

	}
}