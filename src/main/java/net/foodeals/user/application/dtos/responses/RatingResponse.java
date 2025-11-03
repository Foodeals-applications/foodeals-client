package net.foodeals.user.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.core.domain.entities.Rating;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {
    private UUID id;
    private UUID productId;
    private String productName;
    private UUID storeId;
    private String storeName;
    private int rating;
    private String comment;
    private String date;
    private String imageUrl;

    public static RatingResponse fromEntity(Rating ratingEntity) {
        if (ratingEntity == null) return null;

        return new RatingResponse(
                ratingEntity.getId(),
                ratingEntity.getProduct() != null ? ratingEntity.getProduct().getId() : null,
                ratingEntity.getProduct() != null ? ratingEntity.getProduct().getName() : null,
                ratingEntity.getSubEntity() != null ? ratingEntity.getSubEntity().getId() : null,
                ratingEntity.getSubEntity() != null ? ratingEntity.getSubEntity().getName() : null,
                ratingEntity.getRating(),
                ratingEntity.getComment(),
                ratingEntity.getCreatedAt() != null ?
                        DateTimeFormatter.ISO_INSTANT.format(ratingEntity.getCreatedAt()): null,
                ratingEntity.getProduct() != null ? ratingEntity.getProduct().getProductImagePath() : null
        );
    }
}