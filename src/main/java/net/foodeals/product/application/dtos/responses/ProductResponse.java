package net.foodeals.product.application.dtos.responses;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.foodeals.product.domain.enums.ProductType;
import net.foodeals.user.application.dtos.responses.UserResponse;

public record ProductResponse(UUID id, String name, String slug, String title, 
		String description, String imageUrl,
		ProductCategoryResponse category, ProductSubCategoryResponse subCategory,
		String brand, String rayon,
		String barcode, ProductType type, Date creationDate, List<PickupConditionResponse> pickupConditions,
		PaymentMethodResponse paymentMethodResponse, DeliveryMethodResponse deliveryMethodResponse
		,UserResponse createdBy) {

}
