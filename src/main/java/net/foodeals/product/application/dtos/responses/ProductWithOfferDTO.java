package net.foodeals.product.application.dtos.responses;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductWithOfferDTO {

	private String image;
	private String name;
	private BigDecimal newPrice;
	private BigDecimal oldPrice;
	private int remainingStock;
	private boolean addToCart;
}
