package net.foodeals.offer.application.dtos.requests;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItemRequest {
    private UUID productId;
    private int quantity;
    private UUID storeId;
}