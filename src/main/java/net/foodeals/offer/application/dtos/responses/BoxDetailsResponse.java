package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.product.application.dtos.responses.SimilarProductResponse;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class BoxDetailsResponse {
    UUID id;
    String photoPath;
    String title;
    String description;
    Integer numberOfFeedback;
    Float numberOfStars;
    Float estimatedDeliveryTime;
    List<OpenTimeResponse> openTime;
    List<ModalityType>modalityTypes;
    List<SimilarProductResponse>similarProductResponses;
    String categoryName;
}


