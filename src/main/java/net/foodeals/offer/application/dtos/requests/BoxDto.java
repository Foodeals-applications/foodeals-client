package net.foodeals.offer.application.dtos.requests;


import java.util.List;

import jakarta.validation.constraints.NotNull;
import net.foodeals.core.domain.entities.Price;
import net.foodeals.core.domain.enums.*;


public record BoxDto(String title , String description, String photoBoxPath , BoxType type, Price price, Integer reduction, Integer quantity,
                     List<OpenTimeDto> openTimes, List<ModalityType> modalityTypes, ModalityPaiement modalityPaiement,
                     Long deliveryFee, @NotNull PublishAs publishAs, @NotNull Category category) {
}
