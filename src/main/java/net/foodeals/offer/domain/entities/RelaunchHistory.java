package net.foodeals.offer.domain.entities;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "relaunch_history")
@Getter
@Setter
public class RelaunchHistory extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private OrganizationEntity organization;  

    @ManyToOne
    @JoinColumn(name = "box_id", nullable = false)
    private Box box;

    private LocalDateTime relaunchDate;
    
    

    public RelaunchHistory(OrganizationEntity organization, Box box, LocalDateTime relaunchDate) {
        this.organization = organization;
        this.box = box;
        this.relaunchDate = relaunchDate;
    }



	public RelaunchHistory() {
		super();
	}
}
