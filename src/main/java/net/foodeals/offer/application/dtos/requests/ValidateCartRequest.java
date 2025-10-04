package net.foodeals.offer.application.dtos.requests;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateCartRequest {
    private List<UUID> selectedItems;
}