package net.foodeals.offer.application.dtos.requests;

import net.foodeals.core.domain.enums.ModalityPaiement;

import java.awt.*;
import java.util.List;


public record RelaunchDealDto(
        Dialog.ModalityType modalityType,
        ModalityPaiement modalityPaiement, Long deliveryFee, List<OpenTimeDto> openTimes) {
}
