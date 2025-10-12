package net.foodeals.offer.application.dtos.responses;





import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.enums.DealStatus;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record DealResponse(
        UUID id,
        String productName,
        String productDescription,
        String productPhotoPath,
        Date creationDate,
        Integer numberOfOrders,
        Integer numberOfItems,
        DealStatus dealStatus,
        List<SupplementDealResponse> supplementDealResponses
) {

    public static DealResponse fromEntity(Deal deal) {
        if (deal == null) {
            return null;
        }

        // Conversion des supplements
        List<SupplementDealResponse> supplements = deal.getSupplements() != null
                ? deal.getSupplements().stream()
                .map(SupplementDealResponse::fromEntity)
                .collect(Collectors.toList())
                : List.of();

        // ✅ Conversion Instant → Date
        Date createdDate = (deal.getCreatedAt() != null)
                ? Date.from(deal.getCreatedAt())
                : null;

        return new DealResponse(
                deal.getId(),
                deal.getProduct() != null ? deal.getProduct().getName() : null,
                deal.getProduct() != null ? deal.getProduct().getDescription() : null,
                deal.getProduct() != null ? deal.getProduct().getProductImagePath() : null,
                createdDate, // 👈 utilisé ici
                null,
                deal.getQuantity(),
                deal.getDealStatus(),
                supplements
        );
    }
}
