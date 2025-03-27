package net.foodeals.offer.application.dtos.responses;

import java.util.List;

import net.foodeals.common.valueOjects.Price;

public record CartResponse (List<DealCartResponse>deals,int totalOfProducts,Price priceHt,Price priceTva,
		Price priceTotalTTc,Price commissionFoodeals) {

}
