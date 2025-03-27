package net.foodeals.delivery.domain.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.delivery.domain.enums.DeliveryStatus;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(name = "deliveries")

@Getter
@Setter
public class Delivery extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@ManyToOne(cascade = CascadeType.ALL)
	private User deliveryBoy;

	private Integer rating;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private DeliveryPosition deliveryPosition;

	@OneToOne(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Order order;

	private Long duration;

	public Delivery() {

	}

	public Delivery(User deliveryBoy, DeliveryStatus status) {
		this.deliveryBoy = deliveryBoy;
		this.status = status;
	}

	public static Delivery create(User deliveryBoy, DeliveryStatus status) {
		return new Delivery(deliveryBoy, status);
	}

}
