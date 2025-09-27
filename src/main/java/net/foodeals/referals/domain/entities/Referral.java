package net.foodeals.referals.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.user.domain.entities.User;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Referral extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private User sender; // utilisateur qui a envoyé l’invitation

    private String email; // email invité
    private boolean successful; // true si l’invité s’est inscrit
    private double reward;
}
