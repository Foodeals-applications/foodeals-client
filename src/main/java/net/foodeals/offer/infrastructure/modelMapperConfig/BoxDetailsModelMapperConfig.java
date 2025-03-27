package net.foodeals.offer.infrastructure.modelMapperConfig;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.responses.BoxDetailsResponse;
import net.foodeals.offer.application.dtos.responses.BoxItemResponse;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.offer.application.dtos.responses.OfferResponse;
import net.foodeals.offer.application.dtos.responses.OpenTimeResponse;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.BoxItem;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.entities.OpenTime;
import net.foodeals.offer.domain.enums.BoxType;
import net.foodeals.offer.domain.enums.OfferType;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.domain.entities.Product;

@Transactional
@Configuration
@RequiredArgsConstructor
public class BoxDetailsModelMapperConfig {

	private final ModelMapper mapper;

	private final DealRepository dealRepository;
	private final BoxRepository boxRepository;

	private OfferResponse offerResponse = null;

	@PostConstruct
	public void configure() {

		mapper.addConverter(context -> {
			final OpenTime openTime = context.getSource();
			return new OpenTimeResponse(openTime.getId(), openTime.getDate(), openTime.getFrom(), openTime.getTo());
		}, OpenTime.class, OpenTimeResponse.class);

		mapper.addConverter(context -> {
			final BoxItem boxItem = context.getSource();
			Product product = boxItem.getProduct();
			return new BoxItemResponse(boxItem.getPrice(), boxItem.getQuantity(),
					mapper.map(product, ProductResponse.class)

			);
		}, BoxItem.class, BoxItemResponse.class);

		mapper.addConverter(context -> {
			final Box box = context.getSource();
			List<OpenTimeResponse> openTimeResponses = box.getOffer().getOpenTime().stream()
					.map(openTime -> mapper.map(openTime, OpenTimeResponse.class)).collect(Collectors.toList());
			
			return new BoxDetailsResponse(box.getId(), box.getPhotoBoxPath(), box.getTitle(), box.getPublishAs(),
					box.getCategory(), openTimeResponses, box.getDescription(), box.getQuantity(),
					box.getOffer().getModalityTypes(), box.getOffer().getModalityPaiement(), box.getOffer().getPrice(),
					box.getOffer().getReduction(), box.getOffer().getPrice(),box.getOffer().getDeliveryFee());
		}, Box.class, BoxDetailsResponse.class);

	}
}
