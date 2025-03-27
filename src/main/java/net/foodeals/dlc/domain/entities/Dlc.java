package net.foodeals.dlc.domain.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(name = "dlcs")

@Getter
@Setter
public class Dlc extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@OneToOne
	private Product product;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
	private Date expiryDate;

	private Integer quantity;

	private Integer discount = 0;

	private String timeRemaining;

	@ManyToMany
	@JoinTable(name = "dlc_users", joinColumns = @JoinColumn(name = "dlc_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users;

	public Dlc() {
		super();
	}

	public Dlc(Product product, Date expiryDate, Integer quantity) {
		this.product = product;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.timeRemaining = calculateTimeRemaining();
	}

	public static Dlc create(Product product, Date expiryDate, Integer quanInteger) {
		return new Dlc(product, expiryDate, quanInteger);
	}

	private String calculateTimeRemaining() {
		long currentTime = System.currentTimeMillis();
		long expirationTime = expiryDate.getTime();
		long duration = expirationTime - currentTime;

		if (duration < 0) {
			return "Product expired";
		}

		long daysRemaining = TimeUnit.MILLISECONDS.toDays(duration);
		long hoursRemaining = TimeUnit.MILLISECONDS.toHours(duration) % 24;

		return String.format("%02dd/%02dh", daysRemaining, hoursRemaining);
	}

}
