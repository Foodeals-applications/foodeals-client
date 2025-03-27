package net.foodeals.product.domain.exceptions;

import java.util.UUID;

public class DeliveryMethodNotFoundException extends RuntimeException {
    private  UUID id;
    private  String deliveryName;

    public DeliveryMethodNotFoundException(UUID id) {
        super("delivery method  with " + id + " not found");
        this.id = id;
    }
    
    public DeliveryMethodNotFoundException(String deliveryName) {
        super("delivery method  with name " + deliveryName + " not found");
        this.deliveryName = deliveryName;
    }
}
