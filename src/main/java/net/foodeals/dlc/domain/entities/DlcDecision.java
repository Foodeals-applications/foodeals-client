package net.foodeals.dlc.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.organizationEntity.domain.entities.Solution;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "dlc_decisions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DlcDecision extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id; // Identifiant unique pour chaque décision.

    @ManyToOne
    private Dlc dlc; // Le DLC concerné par cette décision.

    @ManyToOne
    @JoinColumn(name = "solution_id", nullable = false)
    private Solution solution; // La solution choisie pour valoriser ce DLC.

    private Integer quantity; // Quantité valorisée pour cette solution.

    private Integer discount; // Réduction appliquée au DLC.

    private Date decisionDate; // Date à laquelle cette décision a été prise.
}
