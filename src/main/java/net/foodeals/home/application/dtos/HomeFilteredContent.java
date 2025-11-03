package net.foodeals.home.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.core.domain.entities.Box;
import net.foodeals.core.domain.entities.Deal;
import net.foodeals.core.domain.entities.SubEntity;
import net.foodeals.organizationEntity.application.dtos.responses.*;

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