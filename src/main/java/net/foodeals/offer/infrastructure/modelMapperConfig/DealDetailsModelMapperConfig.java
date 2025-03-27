package net.foodeals.offer.infrastructure.modelMapperConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.requests.OpenTimeDto;
import net.foodeals.offer.application.dtos.responses.BoxItemResponse;
import net.foodeals.offer.application.dtos.responses.DealDetailsResponse;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.offer.application.dtos.responses.OfferResponse;
import net.foodeals.offer.application.dtos.responses.OpenTimeResponse;
import net.foodeals.offer.application.dtos.responses.ProductDealResponse;
import net.foodeals.offer.application.dtos.responses.SupplementDealResponse;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.BoxItem;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.entities.OpenTime;
import net.foodeals.offer.domain.enums.OfferType;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.entities.ProductSubCategory;
import net.foodeals.product.domain.entities.Supplement;


@Transactional
@Configuration
@RequiredArgsConstructor
public class DealDetailsModelMapperConfig {
	
	
	
	private final ModelMapper mapper;

	private final DealRepository dealRepository;
	private final BoxRepository boxRepository;

	private OfferResponse offerResponse = null;

	@PostConstruct
	public void configure() {

		mapper.addConverter(context -> {
			Deal deal = context.getSource();
			Product product = deal.getProduct();
			Integer numberOfOrders=deal.getOffer().getOrders().size();
			
			UUID productCategoryId=null;
			UUID productSubCategoryId=null;
			if(product.getCategory()!=null) {
				productCategoryId=product.getCategory().getId();
				
			}
			
			if(product.getSubcategory()!=null) {
				productSubCategoryId=product.getSubcategory().getId();
				
			}
			ProductDealResponse productDealResponse=new 
					ProductDealResponse(product.getId(), product.getName(), product.getTitle(), 
							product.getDescription(), product.getBarcode(), product.getType(),
							product.getPrice(), product.getProductImagePath(), productCategoryId,
							productSubCategoryId, product.getBrand(), product.getRayon(), 
							product.getExpirationDate());
			
			List<SupplementDealResponse> supplements=new ArrayList<>();
			for(Supplement supplement:deal.getSupplements()) {
				SupplementDealResponse supplementDealResponse=new SupplementDealResponse(supplement.getId(), supplement.getName(),supplement.getPrice(),
						supplement.getSupplementImagePath());
				supplements.add(supplementDealResponse);
			}
			List<OpenTimeDto>openTimes =new ArrayList<>();;
			for(OpenTime openTime :deal.getOffer().getOpenTime()) {
				OpenTimeDto openTimeDto=new OpenTimeDto(openTime.getDate(), openTime.getFrom(), openTime.getTo());
				openTimes.add(openTimeDto);
			}
			return new DealDetailsResponse(deal.getId(), productDealResponse, 
					numberOfOrders, deal.getOffer().getPrice().amount(),deal.getDealUnityType(),supplements, deal.getOffer().getSalePrice().amount(), deal.getOffer().getReduction(), 
					deal.getOffer().getModalityTypes(), deal.getOffer().getModalityPaiement(), deal.getOffer().getDeliveryFee(), openTimes, 
					deal.getPublishAs(), deal.getCategory());
		}, Deal.class, DealDetailsResponse.class);

		
	}

}
