package net.foodeals.location.domain.entities;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import net.foodeals.location.domain.enums.AddressType;
import org.hibernate.annotations.UuidGenerator;

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

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;

    private String zip;

    @Embedded
    private Coordinates coordinates;

    @ManyToOne
    private City city;

    @ManyToOne
    private Region region;
    
    @ManyToOne
    private Country country ;

    private String idMapCity;

    private String idMapRegion;

    private String idMapCountry;

    @OneToMany(mappedBy = "shippingAddress", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;

    @OneToOne(mappedBy = "address",fetch = FetchType.LAZY)
    @JsonIgnore
    private OrganizationEntity organizationEntity;

    @OneToOne(mappedBy = "address",fetch = FetchType.LAZY)
    @JsonIgnore
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