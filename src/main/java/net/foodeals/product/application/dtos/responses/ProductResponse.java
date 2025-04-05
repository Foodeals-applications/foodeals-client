package net.foodeals.product.application.dtos.responses;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.foodeals.product.domain.enums.ProductType;
import net.foodeals.user.application.dtos.responses.UserResponse;

@Data
@AllArgsConstructor
public class ProductResponse {
	private String image;
	private String name;
	private Double oldPrice;
	private Double newPrice;
	private Integer stock;
}

