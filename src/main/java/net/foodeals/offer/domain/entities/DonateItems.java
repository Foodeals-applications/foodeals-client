package net.foodeals.offer.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.product.domain.entities.Product;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "donates_items")
@Getter
@Setter
public class DonateItems extends AbstractEntity<UUID>{
	
	
	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Product>products ;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Donate donate ;

}
