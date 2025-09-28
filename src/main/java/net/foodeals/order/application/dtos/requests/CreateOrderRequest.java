package net.foodeals.order.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.location.application.dtos.requests.AddressRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private List<CartItemRequest> items;
    private String paymentMethod;
    private AddressRequest deliveryAddress;
    private String promoCode; // optionnel
}