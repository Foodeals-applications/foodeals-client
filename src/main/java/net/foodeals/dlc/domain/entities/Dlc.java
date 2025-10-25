package net.foodeals.dlc.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.dlc.domain.enums.ValorisationType;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.user.domain.entities.User;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
	
	private ValorisationType valorisationType;

	@ManyToMany
	@JoinTable(name = "dlc_users", joinColumns = @JoinColumn(name = "dlc_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users;
	
	@ManyToMany
	@JoinTable(name = "dlc_solutions", joinColumns = @JoinColumn(name = "dlc_id"), inverseJoinColumns = @JoinColumn(name = "solution_id"))
	private List<Solution> solutions;
	
	private String motif;
	
	private String reason ;

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

	
	public void calculateValType() {
		ValorisationType valorisationTypeValue=null;
		int timeRemaining=extractDaysRemaining(calculateTimeRemaining());
		if(timeRemaining>0&&timeRemaining<=10) {
			valorisationType=ValorisationType.URGENTE;
			
		}
		if(timeRemaining>10&&timeRemaining<=20) {
		 valorisationType=ValorisationType.EXIGEE;
		 
		}
		if(timeRemaining>20&&timeRemaining<=30) {
			valorisationType=ValorisationType.SOUHAITABLE;
			
		}
		
		
	}
	public String calculateTimeRemaining() {
        if (this.expiryDate == null) return "Unknown";
        long days = ChronoUnit.DAYS.between(LocalDate.now(), this.expiryDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        if (days < 0) return "Expired";
        if (days == 0) return "Expires today";
        return days + " days";
	}
	
	private int extractDaysRemaining(String timeRemaining) {
		String[] parts = timeRemaining.split("d");
		try {
			return Integer.parseInt(parts[0]);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}


    public Boolean getNotifyOnStatusChange() {
        return null;
    }
}
