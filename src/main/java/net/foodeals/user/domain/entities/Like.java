package net.foodeals.user.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.common.models.AbstractEntity;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "likes")
public class Like extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "sub_entity_id", nullable = false)
    private UUID subEntityId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

}
