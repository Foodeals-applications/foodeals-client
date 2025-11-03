package net.foodeals.home.application.dtos;

import lombok.Data;

@Data
public class HomeFilters {
    private Double maxDistance;   // en km
    private Double minStars;      // note minimale
    private Integer minLikes;     // nb likes minimal
    private Double maxPrice;      // prix max (pour deals/offres)
}