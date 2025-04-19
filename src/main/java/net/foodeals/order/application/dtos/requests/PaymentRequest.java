package net.foodeals.order.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    private String cardNumber;
    private String expiry;
    private String cvv;
    private double amount;
}
