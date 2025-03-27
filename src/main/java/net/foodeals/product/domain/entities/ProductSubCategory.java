package net.foodeals.product.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.foodeals.common.models.AbstractEntity;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_subcategories")

@NoArgsConstructor
@Getter
public class ProductSubCategory extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	private String name;

	private String slug;

	@ManyToOne(cascade = CascadeType.ALL)
	private ProductCategory category;

	@OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL)
	private List<Product> products;

	public ProductSubCategory(String name, String slug) {
		this.name = name;
		this.slug = slug;
	}

	public ProductSubCategory(UUID id, String name, String slug) {
		this.id = id;
		this.name = name;
		this.slug = slug;
	}

	public ProductSubCategory(String name, String slug, ProductCategory category) {
		this.name = name;
		this.slug = slug;
		this.category = category;
	}

	public static ProductSubCategory create(UUID id, String name, String slug) {
		return new ProductSubCategory(id, name, slug);
	}

	public static ProductSubCategory create( String name, String slug, ProductCategory category) {
		return new ProductSubCategory(name, slug, category);
	}

	public static ProductSubCategory create(String name, String slug) {
		return new ProductSubCategory(name, slug);
	}

	public ProductSubCategory setName(String name) {
		this.name = name;
		return this;
	}

	public ProductSubCategory setSlug(String slug) {
		this.slug = slug;
		return this;
	}

	public ProductSubCategory setProducts(List<Product> products) {
		this.products = products;
		return this;
	}

}
