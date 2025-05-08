package net.foodeals.offer.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.offer.domain.enums.ModalityType;

import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class CartRequest {

    private UUID dealId;

    private UUID boxId;

    private int quantity;

    private ModalityType modalityType;
}
