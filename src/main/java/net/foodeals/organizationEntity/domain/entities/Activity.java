package net.foodeals.organizationEntity.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.product.domain.entities.ProductCategory;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "activities")

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Activity extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	private String name;

	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ProductCategory> categories;

	@ManyToMany(mappedBy = "activities", fetch = FetchType.LAZY)
	private List<SubEntity> subEntities= new ArrayList<>() ;

	@ManyToMany(mappedBy = "subActivities", fetch = FetchType.LAZY)
	@Builder.Default
	private List<OrganizationEntity> organizationEntities = new ArrayList();

	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Offer> offers =new ArrayList<>();

	public Activity() {
	}

	public Activity(String name) {
		this.name = name;
	}

	public static Activity create(String name) {
		return new Activity(name);
	}

}