package net.foodeals.offer.application.dtos.requests;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectStoreItemsRequest {
    private UUID storeId;
    private boolean selected;
}