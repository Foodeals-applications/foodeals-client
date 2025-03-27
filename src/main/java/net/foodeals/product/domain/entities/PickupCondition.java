package net.foodeals.product.domain.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "pickup_conditions")

@NoArgsConstructor
@Getter
@Setter
public class PickupCondition extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@Column(name = "condition_name", nullable = false)
	private String conditionName;

	@Column(name = "days_before_pickup", nullable = false)
	private int daysBeforePickup;

	@Column(name = "discount_percentage", nullable = false)
	private int discountPercentage;
	
	@ManyToOne(cascade = CascadeType.ALL)
    private PaymentMethodProduct paymentMethodProduct;

    @ManyToOne(cascade = CascadeType.ALL)
    private DeliveryMethod deliveryMethod;

	@ManyToOne(cascade = CascadeType.ALL)
	private Product product;

	public PickupCondition(UUID id, String conditionName, int daysBeforePickup, int discountPercentage) {
		this.id = id;
		this.conditionName = conditionName;
		this.daysBeforePickup = daysBeforePickup;
		this.discountPercentage = discountPercentage;
	}
	
	public PickupCondition( String conditionName, int daysBeforePickup, int discountPercentage) {
		this.conditionName = conditionName;
		this.daysBeforePickup = daysBeforePickup;
		this.discountPercentage = discountPercentage;
	}
	
	

	public static PickupCondition create(UUID id, String conditionName, int daysBeforePickup, int discountPercentage) {
		return new PickupCondition(id, conditionName, daysBeforePickup, discountPercentage);
	}
	
	
	public static PickupCondition create(String conditionName, int daysBeforePickup, int discountPercentage) {
		return new PickupCondition( conditionName, daysBeforePickup, discountPercentage);
	}

	public PickupCondition setConditionName(String conditionName) {
		this.conditionName = conditionName;
		return this;
	}

	public PickupCondition  setDaysBeforePickup(int daysBeforePickup) {
		this.daysBeforePickup = daysBeforePickup;
		return this;
	}

	public PickupCondition  setPaymentMethod(PaymentMethodProduct paymentMethodProduct) {
		this.paymentMethodProduct = paymentMethodProduct;
		return this;
	}
	
	public PickupCondition  setDeliveryMethod(DeliveryMethod deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
		return this;
	}
	
	public PickupCondition  setProduct(Product product) {
		this.product = product;
		return this;
	}
	
	

	public PickupCondition  setDiscountPercentage(int discountPercentage) {
		this.discountPercentage = discountPercentage;
		return this;
	}
	
	

}
