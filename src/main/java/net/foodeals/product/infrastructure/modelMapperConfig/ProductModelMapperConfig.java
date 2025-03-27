package net.foodeals.product.infrastructure.modelMapperConfig;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.responses.SupplementDealResponse;
import net.foodeals.product.application.dtos.responses.DeliveryMethodResponse;
import net.foodeals.product.application.dtos.responses.PaymentMethodResponse;
import net.foodeals.product.application.dtos.responses.PickupConditionResponse;
import net.foodeals.product.application.dtos.responses.ProductBrandResponse;
import net.foodeals.product.application.dtos.responses.ProductCategoryResponse;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.application.dtos.responses.ProductSubCategoryResponse;
import net.foodeals.product.application.dtos.responses.RayonResponse;
import net.foodeals.product.domain.entities.DeliveryMethod;
import net.foodeals.product.domain.entities.PaymentMethodProduct;
import net.foodeals.product.domain.entities.PickupCondition;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.ProductBrand;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.entities.ProductSubCategory;
import net.foodeals.product.domain.entities.Rayon;
import net.foodeals.product.domain.entities.Supplement;
import net.foodeals.user.application.dtos.responses.UserResponse;

@Configuration
@RequiredArgsConstructor
public class ProductModelMapperConfig {

	private final ModelMapper mapper;

	@PostConstruct
	public void configure() {

		mapper.addConverter(context -> {
			Supplement supplement = context.getSource();

			return new SupplementDealResponse(supplement.getId(),supplement.getName() ,supplement.getPrice(),
					supplement.getSupplementImagePath());

			
		}, Supplement.class, SupplementDealResponse.class);
		
		mapper.addConverter(context -> {
			PickupCondition pickupCondition = context.getSource();

			return new PickupConditionResponse(pickupCondition.getId(), pickupCondition.getConditionName(),
					pickupCondition.getDaysBeforePickup(), pickupCondition.getDiscountPercentage())

			;
		}, PickupCondition.class, PickupConditionResponse.class);

		mapper.addConverter(context -> {
			PaymentMethodProduct paymentMethodProduct = context.getSource();

			return new PaymentMethodResponse(paymentMethodProduct.getId(), paymentMethodProduct.getMethodName()

			);
		}, PaymentMethodProduct.class, PaymentMethodResponse.class);

		mapper.addConverter(context -> {
			DeliveryMethod deliveryMethod = context.getSource();

			return new DeliveryMethodResponse(deliveryMethod.getId(), deliveryMethod.getDeliveryName()

			);
		}, DeliveryMethod.class, DeliveryMethodResponse.class);

		mapper.addConverter(context -> {
			ProductSubCategory category = context.getSource();
			return new ProductSubCategoryResponse(category.getId(), category.getName(), category.getSlug()

			);
		}, ProductSubCategory.class, ProductSubCategoryResponse.class);

		mapper.addConverter(context -> {
			ProductCategory category = context.getSource();
			List<ProductSubCategoryResponse> suCategoryResponses = category.getSubCategories().stream()
					.map(subcategory -> mapper.map(subcategory, ProductSubCategoryResponse.class))
					.collect(Collectors.toList());
			return new ProductCategoryResponse(category.getId(), category.getName(), category.getSlug(),
					suCategoryResponses

			);
		}, ProductCategory.class, ProductCategoryResponse.class);

		mapper.addConverter(context -> {
			ProductSubCategory category = context.getSource();
			return new ProductSubCategoryResponse(category.getId(), category.getName(), category.getSlug()

			);
		}, ProductSubCategory.class, ProductSubCategoryResponse.class);

		mapper.addConverter(context -> {
			ProductBrand brand = context.getSource();
			return new ProductBrandResponse(brand.getId(), brand.getName(), brand.getSlug()

			);
		}, ProductBrand.class, ProductBrandResponse.class);

		mapper.addConverter(context -> {
			Rayon rayon = context.getSource();
			return new RayonResponse(rayon.getId(), rayon.getName()

			);
		}, Rayon.class, RayonResponse.class);

		mapper.addConverter(context -> {
			Product product = context.getSource();
			List<PickupConditionResponse> pickupConditionResponses = null;
			if (product.getPickupConditions() != null) {
				pickupConditionResponses = product.getPickupConditions().stream()
						.map(pickup -> mapper.map(pickup, PickupConditionResponse.class)).collect(Collectors.toList());

			}
			PaymentMethodResponse paymentMethodResponse = null;
			if (product.getPaymentMethodProduct() != null) {
				paymentMethodResponse = mapper.map(product.getPaymentMethodProduct(), PaymentMethodResponse.class);
			}

			DeliveryMethodResponse deliveryMethodResponse = null;
			if (product.getDeliveryMethod() != null) {
				deliveryMethodResponse = mapper.map(product.getDeliveryMethod(), DeliveryMethodResponse.class);
			}
			Date creationDate = product.getCreatedAt() != null ? Date.from(product.getCreatedAt()) : null;
			UserResponse createdBy = null;
			if (product.getCreatedBy() != null) {
				createdBy = mapper.map(product.getCreatedBy(), UserResponse.class);
			}

			ProductCategoryResponse categoryResponse = null;
			ProductSubCategoryResponse subCategoryResponse = null;

			if (product.getCategory() != null) {
				categoryResponse = mapper.map(product.getCategory(), ProductCategoryResponse.class);
			}

			if (product.getSubcategory() != null) {
				subCategoryResponse = mapper.map(product.getSubcategory(), ProductSubCategoryResponse.class);
			}

			return new ProductResponse(product.getId(), product.getName(), product.getSlug(), product.getTitle(),
					product.getDescription(), product.getProductImagePath(), categoryResponse, subCategoryResponse,
					product.getBrand(), product.getRayon(), product.getBarcode(), product.getType(), creationDate,
					pickupConditionResponses, paymentMethodResponse, deliveryMethodResponse, createdBy);
		}, Product.class, ProductResponse.class);
	}
}
