package net.foodeals.user.domain.entities;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "assignments")
@Setter
@Getter
public class WorkSchedule extends AbstractEntity<UUID>{

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;

	private LocalTime morningStart;
	private LocalTime morningEnd;
	private LocalTime afternoonStart;
	private LocalTime afternoonEnd;
	
	@ManyToOne(cascade = CascadeType.ALL)
    private User collaborator;

}
