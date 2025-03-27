package net.foodeals.offer.infrastructure.modelMapperConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.requests.OpenTimeDto;
import net.foodeals.offer.application.dtos.requests.PriceBlockDTO;
import net.foodeals.offer.application.dtos.responses.DealDetailsResponse;
import net.foodeals.offer.application.dtos.responses.DealProResponse;
import net.foodeals.offer.application.dtos.responses.ProductDealResponse;
import net.foodeals.offer.application.dtos.responses.SupplementDealResponse;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.OpenTime;
import net.foodeals.product.domain.entities.PriceBlock;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.Supplement;


@Transactional
@Configuration
@RequiredArgsConstructor
public class DealProResponseModelMapperConfig {
	
	
	
	private final ModelMapper mapper;



	@PostConstruct
	public void configure() {

		mapper.addConverter(context -> {
			Deal deal = context.getSource();
			Product product = deal.getProduct();
			
			UUID idCategory =null;
			if(product.getCategory()!=null) {
				idCategory=product.getCategory().getId();
			}
			
			UUID idSubCategory =null;
			if(product.getSubcategory()!=null) {
				idSubCategory=product.getSubcategory().getId();
			}
			ProductDealResponse productDealResponse=new 
					ProductDealResponse(product.getId(), product.getName(), product.getTitle(), 
							product.getDescription(), product.getBarcode(), product.getType(),
							product.getPrice(), product.getProductImagePath(), idCategory,
						   idSubCategory, product.getBrand(), product.getRayon(), 
							product.getExpirationDate());
			
			
			List<OpenTimeDto>openTimes =new ArrayList<>();;
			for(OpenTime openTime :deal.getOffer().getOpenTime()) {
				OpenTimeDto openTimeDto=new OpenTimeDto(openTime.getDate(), openTime.getFrom(), openTime.getTo());
				openTimes.add(openTimeDto);
			}
			
			List<PriceBlockDTO>defaultPrices =new ArrayList<>();
			
			if(deal.getDefaultPrices()!=null) {
			for(PriceBlock p :deal.getDefaultPrices()) {
				PriceBlockDTO priceBlockDTO=new PriceBlockDTO(p.getQuantity(), p.getPrice());
				defaultPrices.add(priceBlockDTO);
			}
			}
			
			
			List<PriceBlockDTO>customsPrices =new ArrayList<>();
			if(deal.getCustomPrices()!=null) {
			for(PriceBlock p :deal.getCustomPrices()) {
				PriceBlockDTO priceBlockDTO=new PriceBlockDTO(p.getQuantity(), p.getPrice());
				customsPrices.add(priceBlockDTO);
			}}
			
			String organizationName=deal.getCreator().getName();
			String organizationAvatar=deal.getCreator().getAvatarPath();
			
			return new DealProResponse(deal.getQuantity(), productDealResponse, 
					deal.getExpirationDate(),organizationName,organizationAvatar,
					deal.getPublishAs(),
					deal.getCategory(), deal.getDealStatus(), deal.getDealUnityType(), defaultPrices,
					customsPrices, deal.getOffer().getModalityTypes(), deal.getOffer().getModalityPaiement(),
					deal.getOffer().getDeliveryFee(),openTimes);
		}, Deal.class, DealProResponse.class);

		
	}

}
