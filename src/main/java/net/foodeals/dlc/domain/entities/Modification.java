package net.foodeals.dlc.domain.entities;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.user.domain.entities.User;

@Entity
@Table(name = "modification")

@Getter
@Setter
public class Modification extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@ManyToOne
	private Dlc dlc;

	@ManyToOne
	private User user; 

	private int previousQuantity; 
	private int modifiedQuantity; 

	private int previousDiscount; 
	private int modifiedDiscount; 

	private Date modificationDate;
	
	public Modification() {
		super();
	}

	public Modification(Dlc dlc, User user, int previousQuantity, int modifiedQuantity, int previousDiscount,
			int modifiedDiscount) {
		super();
		this.dlc = dlc;
		this.user = user;
		this.previousQuantity = previousQuantity;
		this.modifiedQuantity = modifiedQuantity;
		this.previousDiscount = previousDiscount;
		this.modifiedDiscount = modifiedDiscount;
	}
	
	public static  Modification create(Dlc dlc, User user, int previousQuantity, int modifiedQuantity, int previousDiscount,
			int modifiedDiscount) {
		return new Modification(
				dlc, user, previousQuantity, modifiedQuantity, previousDiscount, modifiedDiscount);
	}

	
	
	
	
}
