package net.foodeals.support.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.user.domain.entities.User;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicket extends AbstractEntity<UUID>
{
    @Id
    @GeneratedValue
    private UUID id;

    private String subject;
    private String message;
    private String category;
    private String status = "OPEN";
    @ManyToOne
    private User user;
}
