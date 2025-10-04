package net.foodeals.offer.application.dtos.requests;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateQuantityRequest {
    private UUID itemId;
    private int quantity;
}
