package net.foodeals.product.domain.entities;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "product_brand")

@NoArgsConstructor
@Getter
public class ProductBrand extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	private String name;

	private String slug;

	@OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
	private List<Product> products;
	
	public ProductBrand(String name, String slug) {
		this.name = name;
		this.slug = slug;
	}
	
	public ProductBrand(UUID id,String name, String slug) {
		this.id=id;
		this.name = name;
		this.slug = slug;
	}


	public static ProductBrand create(UUID id ,String name, String slug) {
		return new ProductBrand(id,name, slug);
	}
	
	public static ProductBrand create(String name, String slug) {
		return new ProductBrand(name, slug);
	}

	public ProductBrand setName(String name) {
		this.name = name;
		return this;
	}

	public ProductBrand setSlug(String slug) {
		this.slug = slug;
		return this;
	}

	public ProductBrand setProducts(List<Product> products) {
		this.products = products;
		return this;
	}

	

}
