package net.foodeals.offer.application.dtos.requests;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import net.foodeals.offer.domain.entities.Deal.DealUnityType;
import net.foodeals.offer.domain.enums.Category;
import net.foodeals.offer.domain.enums.PublishAs;
import net.foodeals.offer.domain.enums.ModalityPaiement;
import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.application.dtos.requests.SupplementDto;

public record DealDto(@NotNull ProductRequest produtRequest,UUID productId, @NotNull Integer quantity, 
		@NotNull  BigDecimal price, DealUnityType dealUnityType,
		List<SupplementDto> supplements, List<UUID> supplementsIds,  BigDecimal salePrice, 
		Integer reduction,List<ModalityType> modalityTypes, 
		ModalityPaiement modalityPaiement,Long deliveryFee,List<OpenTimeDto> openTimes,
		@NotNull PublishAs publishAs,@NotNull Category category) {
}
