package net.foodeals.product.application.dtos.responses;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductCategoryDTO {

	private String categoryName;
	private List<ProductDto> products;

}
