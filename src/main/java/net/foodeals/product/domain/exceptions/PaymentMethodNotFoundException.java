package net.foodeals.product.domain.exceptions;

import java.util.UUID;

public class PaymentMethodNotFoundException extends RuntimeException {
    private  UUID id;
    private  String methodName;

    public PaymentMethodNotFoundException(UUID id) {
        super("payement method  with " + id + " not found");
        this.id = id;
    }
    
    public PaymentMethodNotFoundException(String methodName) {
        super("payement method  with name " + methodName + " not found");
        this.methodName = methodName;
    }
}
