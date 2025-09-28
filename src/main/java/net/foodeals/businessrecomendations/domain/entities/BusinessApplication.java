package net.foodeals.businessrecomendations.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import net.foodeals.businessrecomendations.domain.enums.ApplicationStatus;
import net.foodeals.common.models.AbstractEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessApplication extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String businessName;
    private String ownerName;
    private String email;
    private String phone;
    private String address;
    private String category;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private Instant submittedAt;
}
