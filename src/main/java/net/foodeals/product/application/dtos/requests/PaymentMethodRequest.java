package net.foodeals.product.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;


public record PaymentMethodRequest(@NotBlank String methodName) {

}
