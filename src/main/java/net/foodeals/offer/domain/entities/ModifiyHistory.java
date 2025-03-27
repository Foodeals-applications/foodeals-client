package net.foodeals.offer.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;

@Entity
@Table(name = "modify_history")
@Getter
@Setter
public class ModifiyHistory extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private OrganizationEntity organization;  

    @ManyToOne
    @JoinColumn(name = "box_id", nullable = false)
    private Box box;

    private LocalDateTime modifyDate;
    
    

    public ModifiyHistory(OrganizationEntity organization, Box box, LocalDateTime modifyDate) {
        this.organization = organization;
        this.box = box;
        this.modifyDate = modifyDate;
    }



	public ModifiyHistory() {
		super();
	}
}