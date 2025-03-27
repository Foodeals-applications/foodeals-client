package net.foodeals.offer.application.dtos.requests;

import net.foodeals.common.valueOjects.Price;
import net.foodeals.offer.domain.enums.BoxType;
import net.foodeals.offer.domain.enums.Category;
import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.offer.domain.enums.PublishAs;

import java.util.List;

import jakarta.validation.constraints.NotNull;


public record BoxDto(String title ,String description, String photoBoxPath , BoxType type, Price price, Integer reduction, Integer quantity,
		List<OpenTimeDto> openTimes, List<ModalityType> modalityTypes, ModalityPaiement modalityPaiement,
		Long deliveryFee,@NotNull PublishAs publishAs,@NotNull Category category) {
}
