package net.foodeals.order.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.entities.Transaction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderConfirmationResponse {
    private OrderPaymentResponse order;
    private TransactionOrderResponse paymentDetails;
}