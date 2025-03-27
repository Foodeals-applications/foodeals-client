package net.foodeals.offer.application.services;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import net.foodeals.common.valueOjects.Price;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.offer.application.dtos.responses.DealCartResponse;
import net.foodeals.offer.domain.entities.Cart;
import net.foodeals.offer.domain.entities.CartItem;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.order.domain.entities.Order;

public class CartMapper {

	public static CartResponse toCartResponse(Cart cart) {
		
		List<DealCartResponse> dealResponses = cart.getItems().stream().map(CartMapper::toDealCartResponse)
				.collect(Collectors.toList());

		
		int totalOfProducts = cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();

		BigDecimal totalHt = cart.getItems().stream()
				.map(item -> item.getDeal().getPrice().amount().multiply(BigDecimal.valueOf(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalTva = totalHt.multiply(BigDecimal.valueOf(0.2));

		BigDecimal totalTtc = totalHt.add(totalTva);
		
		
		BigDecimal commisonFoodeals = totalHt.multiply(BigDecimal.valueOf(0.3));;

		return new CartResponse(dealResponses, totalOfProducts, new Price(totalHt,Currency.getInstance("MAD")), 
				new Price(totalTva,Currency.getInstance("MAD")),
				new Price(totalTtc,Currency.getInstance("MAD")),
				new Price(commisonFoodeals,Currency.getInstance("MAD")));
	}

	private static DealCartResponse toDealCartResponse(CartItem item) {
		
		Date dealDate = item.getDeal().getCreatedAt() != null ? Date.from(item.getDeal().getCreatedAt()) : null;

		LocalTime hourOfDeal =item.getDeal().getCreatedAt() != null
				? item.getDeal().getCreatedAt().atZone(ZoneId.systemDefault()).toLocalTime()
				: null;
		ModalityPaiement modalityPaiement=item.getDeal().getOffer().getModalityPaiement();
		List<ModalityType>modalityTypes =item.getDeal().getOffer().getModalityTypes();
		String addressDelivery =null;
		Date orderDate =null;
	    Offer offer = item.getDeal().getOffer();

	    for (Order order : offer.getOrders()) {
	       
	        if (order.getOffer().equals(item.getDeal().getOffer())) {
	            
	             addressDelivery=order.getShippingAddress().getAddress()+ " "+
	            		 order.getShippingAddress().getCity().getName();
	             orderDate= order.getCreatedAt() != null ? Date.from(order.getCreatedAt()) : null;
	        }
	    } 
		
	    if(modalityPaiement==null) {
	    	modalityPaiement=ModalityPaiement.CARD;
	    }
	    if(modalityTypes==null) {
	    	modalityTypes=List.of(ModalityType.AT_PLACE);
	    }
		return new DealCartResponse(item.getDeal().getId(),
				item.getDeal().getProduct().getTitle(),item.getDeal().getProduct().getDescription(),
				dealDate,hourOfDeal,
				item.getDeal().getCreator().getName(),
				item.getDeal().getCreator().getAvatarPath(),item.getDeal().getPrice(),item.getDeal().getQuantity(),
				orderDate,modalityPaiement,modalityTypes,addressDelivery);
	}
}
