package net.foodeals.product.application.dtos.responses;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.foodeals.product.domain.enums.ProductType;
import net.foodeals.user.application.dtos.responses.UserResponse;

@Data
@AllArgsConstructor
@Builder
public class ProductResponse {
	private UUID id ;
	private String image;
	private String name;
	private String description;
	private PriceResponse price ;
	private List<String>categories ;
	private Integer stock;
	private UUID subEntityId ;
}

