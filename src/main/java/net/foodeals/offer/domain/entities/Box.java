package net.foodeals.offer.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.offer.domain.enums.BoxStatus;
import net.foodeals.offer.domain.enums.BoxType;
import net.foodeals.offer.domain.enums.Category;
import net.foodeals.offer.domain.enums.PublishAs;
import net.foodeals.offer.domain.enums.OfferType;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.Supplement;

import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "boxes")

@Getter
@Setter
public class Box extends AbstractEntity<UUID> implements IOfferChoice {

	
	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;
	
	private String title ;
	
	private String description;

	@Enumerated(EnumType.STRING)
	private BoxType type;
	
	private Integer quantity;

	private String photoBoxPath ;
	

	@ManyToOne(cascade = CascadeType.ALL)
	private Offer offer;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PublishAs publishAs;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;
	
	@Enumerated(EnumType.STRING)
	private BoxStatus boxStatus ;

	@ManyToMany
	@JoinTable(
			name = "box_products",
			joinColumns = @JoinColumn(name = "box_id"),
			inverseJoinColumns = @JoinColumn(name = "product_id")
	)
	private List<Product>products=new ArrayList<>();
	
	
	@OneToMany(mappedBy = "box", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Supplement> supplements;


	@Override
	public OfferType getOfferType() {
		return OfferType.BOX;
	}
    
	
	private String reason ;
	
	private String motif ;
	
	Box() {
	}

	public Box(BoxType type) {
		this.type = type;
	
	}

	public static Box create(BoxType type) {
		return new Box(type);
	}

	
	public Box(String title ,String description,BoxType type, PublishAs publishAs, Category category, Offer offer) {
		this.title=title;
		this.description=description;
		this.type = type;
		this.offer = offer;
		this.publishAs = publishAs;
		this.category = category;
	}


	
	public static Box create(String title ,String description,BoxType type, PublishAs publishAs, Category category,
			Offer offer) {
		return new Box(title , description,type, publishAs, category, offer);
	}

}
