package net.foodeals.organizationEntity.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.filters.application.dtos.DomainResponse;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponse {
    private UUID id;
    private String name;
    private String avatarPath;
    private String coverPath;
    private double distance;  // calculé côté service
    private Integer numberOfLikes;
    private Float numberOfStars;
    private List<DomainResponse> domains;
}