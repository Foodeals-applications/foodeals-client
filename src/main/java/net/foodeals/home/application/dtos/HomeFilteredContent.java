package net.foodeals.home.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.organizationEntity.application.dtos.responses.*;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.offer.domain.entities.Box;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeFilteredContent {
    private List<Deal> deals;
    private List<RestaurantResponse> restaurants;
    private List<BakeryResponse> bakeries;
    private List<HotelResponse> hotels;
    private List<Box> boxes;
    private List<AgricultureResponse> farmers;
    private List<SubEntity> industrials;
    private List<BestSellerResponse> bestSellers;
}