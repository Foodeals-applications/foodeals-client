package net.foodeals.offer.application.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private double deliveryCost;
    private double totalPrice;
    private List<CartItemResponse> cartsItemsResponse;

}
