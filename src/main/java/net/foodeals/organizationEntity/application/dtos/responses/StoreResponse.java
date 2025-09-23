package net.foodeals.organizationEntity.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}