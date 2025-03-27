package net.foodeals.product.domain.entities;


import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.organizationEntity.domain.entities.Activity;

@Entity
@Table(name = "product_categories")

@NoArgsConstructor
@Getter
public class ProductCategory extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	private String name;

	private String slug;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Activity activity;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Product> products;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<ProductSubCategory> subCategories;

	public ProductCategory(String name, String slug) {
		this.name = name;
		this.slug = slug;
	}

	public ProductCategory(UUID id, String name, String slug) {
		this.id = id;
		this.name = name;
		this.slug = slug;
	}

	public static ProductCategory create(UUID id, String name, String slug) {
		return new ProductCategory(name, slug);
	}

	public static ProductCategory create(String name, String slug) {
		return new ProductCategory(name, slug);
	}

	public ProductCategory setName(String name) {
		this.name = name;
		return this;
	}

	public ProductCategory setSlug(String slug) {
		this.slug = slug;
		return this;
	}

	public ProductCategory setProducts(List<Product> products) {
		this.products = products;
		return this;
	}
}
