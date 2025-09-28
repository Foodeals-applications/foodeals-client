package net.foodeals.order.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.offer.application.dtos.responses.CartItemResponse;
import net.foodeals.product.application.dtos.responses.PaymentMethodResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutDataResponse {
    private List<CartItemResponse> items;
    private List<PaymentMethodResponse> paymentMethods;
    private List<DeliveryOptionResponse> deliveryOptions;
}
