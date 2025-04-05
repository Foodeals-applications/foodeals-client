package net.foodeals.organizationEntity.domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.contract.domain.entities.Contract;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.offer.domain.entities.DonorInfo;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.entities.PublisherI;
import net.foodeals.offer.domain.entities.ReceiverInfo;
import net.foodeals.offer.domain.enums.DonationReceiverType;
import net.foodeals.offer.domain.enums.DonorType;
import net.foodeals.offer.domain.enums.PublisherType;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityStatus;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;
import net.foodeals.payment.domain.entities.PartnerI;
import net.foodeals.payment.domain.entities.Enum.PartnerType;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(name = "sub_entities")

@Getter
@Setter
public class SubEntity extends AbstractEntity<UUID> implements DonorInfo, ReceiverInfo, PublisherI, PartnerI {

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
	private String iFrame;

	@Embedded
	private Coordinates coordinates;

	@OneToOne(cascade = CascadeType.ALL)
	private User manager;

	@ManyToOne(optional = true, cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private OrganizationEntity organizationEntity;

	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Activity> activities = new ArrayList<>();

	@OneToMany(mappedBy = "subEntity", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
	private List<User> users=new ArrayList<>();

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
    private List<Offer> offers =new ArrayList<>();
	
	@ManyToOne
	private Contract contract;

	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Solution> solutions = new ArrayList<>();

	private String email;

	private String phone;

	private String reason;

	private String motif;

	private Integer numberOfLikes ;
	
	@Enumerated(EnumType.STRING)
	private SubEntityStatus subEntityStatus;
	
	

	public SubEntity() {
	}

	public PartnerType getPartnerType() {
		return PartnerType.SUB_ENTITY;
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
		return null ;
	}

	@Override
	@Transactional
	public boolean subscriptionPayedBySubEntities() {
		// return this.contract.isSubscriptionPayedBySubEntities();
		return false;
	}

	@Override
	public boolean singleSubscription() {
		// return this.contract.isSingleSubscription();
		return false;
	}

	@Override
	public DonationReceiverType getReceiverType() {
		/*return switch (this.type) {
		case FOOD_BANK_ASSOCIATION -> DonationReceiverType.FOOD_BANK_ASSOCIATION;
		case FOOD_BANK_SB -> DonationReceiverType.FOOD_BANK_SB;
		default -> null;
		};*/
		return null ;
	}

	@Override
	public DonorType getDonorType() {
		/* switch (type) {
		case PARTNER_SB -> DonorType.PARTNER_SB;
		case FOOD_BANK_SB -> DonorType.FOOD_BANK_SB;
		default -> null;
		};*/
		return null ;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getAvatarPath() {
		return this.avatarPath;
	}

	@Override
	public boolean commissionPayedBySubEntities() {
		return this.organizationEntity.getContract().isCommissionPayedBySubEntities();
	}
}
