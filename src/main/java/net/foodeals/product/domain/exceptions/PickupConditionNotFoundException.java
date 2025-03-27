package net.foodeals.product.domain.exceptions;

import java.util.UUID;

public class PickupConditionNotFoundException extends RuntimeException {
    private  UUID id;
    private  String conditionName;

    public PickupConditionNotFoundException(UUID id) {
        super("pickup condition  with " + id + " not found");
        this.id = id;
    }
    
    public PickupConditionNotFoundException(String conditionName) {
        super("pickup condition  with name " + conditionName + " not found");
        this.conditionName = conditionName;
    }
}
