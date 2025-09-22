package net.foodeals.organizationEntity.application.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SpotlightStore {
    private UUID id;
    private String name;
    private String avatarPath;
    private String coverPath;
    private double distance;      // distance depuis l'utilisateur connecté
    private Integer numberOfLikes;
    private Float numberOfStars;
}