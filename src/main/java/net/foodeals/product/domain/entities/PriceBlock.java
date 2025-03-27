package net.foodeals.product.domain.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import net.foodeals.common.valueOjects.Price;

@Entity
@Table(name = "price_block")
public class PriceBlock {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	private Integer quantity;

	private Price price;

	public PriceBlock() {
		super();
	}

	public PriceBlock(Integer quantity, Price price) {
		this.quantity = quantity;
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}
}