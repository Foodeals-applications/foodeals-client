package net.foodeals.organizationEntity.domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import net.foodeals.offer.domain.enums.*;
import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.offer.domain.entities.DonorInfo;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.entities.PublisherI;
import net.foodeals.offer.domain.entities.ReceiverInfo;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityStatus;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(name = "sub_entities")

@Getter
@Setter
public class SubEntity extends AbstractEntity<UUID> implements DonorInfo, ReceiverInfo, PublisherI {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)

    private SubEntityType type;

    @Column(name = "avatar_path")
    private String avatarPath;

    @Column(name = "cover_path")
    private String coverPath;

    @Column(name = "iframe", length = 800)
    @JsonIgnore
    private String iFrame;

    @Embedded
    private Coordinates coordinates;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private User manager;

    @ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private OrganizationEntity organizationEntity;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "subEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = true, unique = true)
    private Address address;

//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	@Builder.Default
//	private List<DeletionReason> deletionReasons = new ArrayList<>();
//
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Notification> notifications;
//
//	@OneToMany(mappedBy = "subEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Coupon> coupons;
//
//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<PartnerCommissions> commissions = new ArrayList<>();
//
//	@Builder.Default
//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Subscription> subscriptions = new ArrayList<>();
//	
//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Offer> offers =new ArrayList<>();
//

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Offer> offers = new ArrayList<>();


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Solution> solutions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private List<ModalityType>modalityTypes;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private List<ModalityPaiement>modalityPaiements;

    private String email;

    private String phone;

    private String reason;

    private String motif;

    private Integer numberOfLikes;

    private Float numberOfStars;

    private boolean feeDelivered;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "subentity_domains", joinColumns = @JoinColumn(name = "subentity_id"), inverseJoinColumns = @JoinColumn(name = "domain_id"))

    private List<SubEntityDomain> subEntityDomains = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private SubEntityStatus subEntityStatus;


    public SubEntity() {
    }


    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public PublisherType getPublisherType() {
		/*return switch (this.type) {
		case PARTNER_SB -> PublisherType.PARTNER_SB;
		case FOOD_BANK_SB -> PublisherType.FOOD_BANK_SB;
		default -> null;
		};*/
        return null;
    }





    @Override
    public DonationReceiverType getReceiverType() {
		/*return switch (this.type) {
		case FOOD_BANK_ASSOCIATION -> DonationReceiverType.FOOD_BANK_ASSOCIATION;
		case FOOD_BANK_SB -> DonationReceiverType.FOOD_BANK_SB;
		default -> null;
		};*/
        return null;
    }

    @Override
    public DonorType getDonorType() {
		/* switch (type) {
		case PARTNER_SB -> DonorType.PARTNER_SB;
		case FOOD_BANK_SB -> DonorType.FOOD_BANK_SB;
		default -> null;
		};*/
        return null;
    }





    public void addSubEntityDomain(SubEntityDomain domain) {
        this.subEntityDomains.add(domain);
    }

}
