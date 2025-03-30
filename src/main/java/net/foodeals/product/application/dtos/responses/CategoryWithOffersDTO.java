package net.foodeals.product.application.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryWithOffersDTO {

	private String categoryName;
	private boolean isFavorite;

}
