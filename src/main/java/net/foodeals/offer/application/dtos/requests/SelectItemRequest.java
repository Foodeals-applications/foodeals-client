package net.foodeals.offer.application.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SelectItemRequest {
    private UUID itemId;
    private boolean selected;
}