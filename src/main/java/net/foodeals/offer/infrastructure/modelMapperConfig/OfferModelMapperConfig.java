package net.foodeals.offer.infrastructure.modelMapperConfig;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.responses.BoxItemResponse;
import net.foodeals.offer.application.dtos.responses.DealResponse;
import net.foodeals.offer.application.dtos.responses.OfferResponse;
import net.foodeals.offer.application.dtos.responses.OpenTimeResponse;
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

@Transactional
@Configuration
@RequiredArgsConstructor
public class OfferModelMapperConfig {

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
			String photoBox=null;
			photoBox=box.getPhotoBoxPath();
			Date creationDate = box.getCreatedAt() != null ? Date.from(box.getCreatedAt()) : null;
			Integer numberOfOrders=box.getOffer().getOrders().size();
			Integer numberOfItems=box.getQuantity();
			return new BoxResponse(box.getId(),box.getTitle(),box.getDescription(),box.getType(),photoBox,
					creationDate,numberOfOrders,numberOfItems,box.getBoxStatus(),"box");
		}, Box.class, BoxResponse.class);

		mapper.addConverter(context -> {
			Deal deal = context.getSource();
			Date creationDate = deal.getCreatedAt() != null ? Date.from(deal.getCreatedAt()) : null;
			Product product = deal.getProduct();
			Integer numberOfOrders=deal.getOffer().getOrders().size();
			Integer numberOfItems=deal.getQuantity();
			return new DealResponse(deal.getId(), deal.getProduct().getName(), deal.getProduct().getDescription(),
					product.getProductImagePath(),creationDate,numberOfOrders,numberOfItems,
					deal.getDealStatus(),"deal");
		}, Deal.class, DealResponse.class);

		mapper.addConverter(context -> {
			final Offer offer = context.getSource();
			final List<OpenTimeResponse> openTimeResponses = offer.getOpenTime().stream()
					.map((element) -> mapper.map(element, OpenTimeResponse.class)).toList();
			String photoURL = null;
			if (offer.getOfferable().type().equals(OfferType.DEAL)) {
				Deal deal = dealRepository.getDealByOfferId(offer.getId());
				offerResponse = new OfferResponse(offer.getId(), offer.getPrice(), offer.getSalePrice(),
						offer.getReduction(), offer.getBarcode(), openTimeResponses, offer.getOfferable(),
						offer.getOfferable().type(), mapper.map(deal, DealResponse.class), null);
			}

			else {

			}
			if (offer.getOfferable().type().equals(OfferType.BOX)) {
				Box box = boxRepository.getBoxByOfferId(offer.getId());

				offerResponse = new OfferResponse(offer.getId(), offer.getPrice(), offer.getSalePrice(),
						offer.getReduction(), offer.getBarcode(), openTimeResponses, offer.getOfferable(),
						offer.getOfferable().type(), null, mapper.map(box, BoxResponse.class));
			}
			return offerResponse;
		}, Offer.class, OfferResponse.class);
	}
}
