package net.foodeals.user.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "search_history")
public class SearchHistory  extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	private UUID id;

	private String keyword;

	private Integer userId; // Identifiant de l'utilisateur

	private LocalDateTime searchedAt = LocalDateTime.now();
}
