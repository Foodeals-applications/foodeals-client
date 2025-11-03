package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.core.domain.enums.ModalityType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class DealDetailsResponse {

	private UUID id;
	private String photoPath;
	private String title;
	private String description;
	private Integer quantity;
	private double distance;
	private Integer numberOfFeedback;
	private Float numberOfStars;
	private Float reviews;
	private Float estimatedDeliveryTime;
	private List<OpenTimeResponse> openTime;
	private List<ModalityType>modalityTypes;
	private String categoryName;
	private String address ;
	private Boolean favorite;
	private BigDecimal oldPrice;
	private BigDecimal newPrice;
	private Integer discount;
	private Map<String ,List<SupplementDealResponse>> supplementResponses;
	private List<SimilarDealResponse>similarDeals;


}
