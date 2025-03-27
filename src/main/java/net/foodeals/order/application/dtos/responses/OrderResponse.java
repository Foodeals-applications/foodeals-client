package net.foodeals.order.application.dtos.responses;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.foodeals.common.valueOjects.Price;
import net.foodeals.order.domain.enums.OrderSource;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.enums.OrderType;


public record OrderResponse(UUID id,UUID idDealPro, OrderType type, OrderStatus status,OrderSource orderSource,
		String client, String clientProAvatar,String clientProActivity ,String typeOffer,
		List<String>photosProducts,String barCode,String title,String description,Integer quantity,
		Date orderDate,Price priceOrder,Price oldPrice ,String offerCreatorName, String offerCreatorAvatar,boolean seen,boolean affected) {

}
