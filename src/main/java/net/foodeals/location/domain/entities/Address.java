package net.foodeals.location.domain.entities;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;

@Entity
@Table(name = "address")

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String address;

    @Column(name = "extra_address")
    private String extraAddress;

    private String zip;

    @Embedded
    private Coordinates coordinates;

    @ManyToOne
    private City city;

    @ManyToOne
    private Region region;
    
    @ManyToOne
    private Country country ;

    @OneToMany(mappedBy = "shippingAddress", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Order> orders;

    @OneToOne(mappedBy = "address",fetch = FetchType.LAZY)
    private OrganizationEntity organizationEntity;

    @OneToOne(mappedBy = "address",fetch = FetchType.LAZY)
    private SubEntity subEntity;

	public Address(String address, Coordinates coordinates, City city, Region region,Country country) {
		super();
		this.address = address;
		this.coordinates = coordinates;
		this.city = city;
		this.region = region;
		this.country=country;
	}
    
    
}