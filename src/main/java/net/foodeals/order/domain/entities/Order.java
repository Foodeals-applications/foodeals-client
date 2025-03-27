package net.foodeals.order.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.delivery.domain.entities.Delivery;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.order.domain.enums.OrderSource;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.enums.OrderType;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(name = "orders")
@Getter
public class Order extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_type")
	private OrderType type;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ManyToOne(cascade = CascadeType.ALL)
	private User client;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private SubEntity clientPro;

	@ManyToOne(cascade = CascadeType.ALL)
	private Offer offer;

	@ManyToOne(cascade = CascadeType.ALL)
	private Address shippingAddress;

	@OneToOne(cascade = CascadeType.ALL)
	private Delivery delivery;

	@ManyToOne(cascade = CascadeType.ALL)
	private Coupon coupon;
	
	private Integer quantity ;
	
	private Integer quantityOfOrder;

	@OneToOne(cascade = CascadeType.ALL)
	private Transaction transaction;

	@Column(name = "cancellation_reason")
	private String cancellationReason;

	@Column(name = "cancellation_subject")
	private String cancellationSubject;

	@Column(name = "cancellation_date")
	private LocalDateTime cancellationDate;

	@Column(name = "attachment_path")
	private String attachmentPath;

	@Enumerated(EnumType.STRING)
	private OrderSource orderSource;
	
	private boolean seen ;

	public Order() {
	}

	public Order(OrderType type, OrderStatus status, User client, Offer offer) {
		this.type = type;
		this.status = status;
		this.client = client;
		this.offer = offer;
	}

	public static Order create(OrderType type, OrderStatus status, User client, Offer offer) {
		return new Order(type, status, client, offer);
	}

	public Order setOrderType(OrderType orderType) {
		this.type = orderType;
		return this;
	}

	public Order setOrderSource(OrderSource orderSource) {
		this.orderSource = orderSource;
		return this;
	}

	public Order setStatus(OrderStatus orderStatus) {
		this.status = orderStatus;
		return this;
	}

	public Order setShippingAddress(Address address) {
		this.shippingAddress = address;
		return this;
	}

	public Order setClient(User client) {
		this.client = client;
		return this;
	}
	
	public Order setClientPro(SubEntity clientPro) {
		this.clientPro = clientPro;
		return this;
	}

	public Order setOffer(Offer offer) {
		this.offer = offer;
		return this;
	}

	public Order setDelivery(Delivery delivery) {
		this.delivery = delivery;
		return this;
	}

	public Order setCoupon(Coupon coupon) {
		this.coupon = coupon;
		return this;
	}

	public Order setTransaction(Transaction transaction) {
		this.transaction = transaction;
		return this;
	}

	public Order setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
		return this;
	}

	public Order setCancellationSubject(String cancellationSubject) {
		this.cancellationSubject = cancellationSubject;
		return this;
	}

	public Order setCancellationDate(LocalDateTime cancellationDate) {
		this.cancellationDate = cancellationDate;
		return this;
	}

	public Order setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
		return this;
	}
	
	public Order setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}
	
	public Order setQuantityOfOrder(Integer quantityOfOrder) {
		this.quantityOfOrder = quantityOfOrder;
		return this;
	}

	public Order setSeen(boolean seen) {
		this.seen = seen;
		return this;
	}
	

}
