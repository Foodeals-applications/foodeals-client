package net.foodeals.product.infrastructure.interfaces.web;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.foodeals.organizationEntity.application.dtos.responses.SubEntityResponse;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.product.application.dtos.responses.ProductDetailsResponse;
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

@RestController
@RequestMapping("v1/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	private final SubEntityService subEntityService;
	private final ProductRepository productRepository;
	private final PaymentMethodRepository paymentMethodRepository;
	private final DeliveryMethodRepository deliveryMethodRepository;
	private final PickupConditionRepository pickupConditionRepository;
	private final ModelMapper mapper;



	@GetMapping("/{productId}/details")
	public ResponseEntity<ProductDetailsResponse> getProductDetails(@PathVariable UUID productId) {

		// 1. Récupérer les détails du produit
		ProductResponse product = productService.getProductDetails(productId);

		// 2. Récupérer les détails du magasin lié au produit
	//	SubEntityResponse store = subEntityService.getSubEntityDetails(product.getSubEntityId());

		// 3. Récupérer les avis pour le produit
		//ReviewResponse reviews = reviewsService.getProductReviews(productId);

		// 4. Vérifier les modalités et la livraison
		//DeliveryResponse delivery = deliveryService.getDeliveryOptions(productId, store.getId());

		// 5. Obtenir des produits similaires dans la même catégorie
		//List<ProductSuggestionResponse> similarProducts = suggestionService.getSimilarProducts(product);

		// 6. Construire la réponse finale
		ProductDetailsResponse response = new ProductDetailsResponse(
			null, product, null, null, null
		);

		return ResponseEntity.ok(response);
	}








}