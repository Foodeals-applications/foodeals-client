package net.foodeals.order.infrastructure.modelMapperConfig;


import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.location.application.dtos.responses.AddressResponse;
import net.foodeals.offer.application.dtos.responses.OfferResponse;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.BoxItem;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.enums.OfferType;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.order.application.dtos.responses.CouponResponse;
import net.foodeals.order.application.dtos.responses.OrderResponse;
import net.foodeals.order.application.dtos.responses.TransactionResponse;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.entities.Transaction;
import net.foodeals.order.domain.enums.OrderSource;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityResponse;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.repositories.SubEntityRepository;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.user.application.dtos.responses.UserResponse;
import net.foodeals.user.domain.valueObjects.Name;

@Configuration
@RequiredArgsConstructor
@Transactional
public class OrderModelMapperConfig {

	private final ModelMapper mapper;
	private final DealRepository dealRepository;
	private final BoxRepository boxRepository;
	private final SubEntityRepository subEntityRepository;

	@PostConstruct
	public void configure() {

		mapper.addConverter(context -> {
			Order order = context.getSource();
			List<String> photosProducts = new ArrayList<>();
			String title = null;
			String description = null;
			String barCode = null;
			Offer offer = order.getOffer();
			String offerCreatorName=null;
			String offerCreatorAvatar=null;
			String typeOffer=null;
			UUID idDeal=null;
			if (offer.getOfferable().type().equals(OfferType.DEAL)) {
				Deal deal = dealRepository.getDealByOfferId(offer.getId());
				typeOffer="DEAL";
				offerCreatorName=deal.getCreator().getName();
				offerCreatorAvatar=deal.getCreator().getAvatarPath();
				idDeal=deal.getId();
				Product product = deal.getProduct();
				photosProducts.add(product.getProductImagePath());
				title = product.getTitle();
				description = product.getDescription();
				if (order.getStatus().compareTo(OrderStatus.IN_PROGRESS) == 0) {
					barCode = product.getBarcode();
				}

			} else {
				Box box = boxRepository.getBoxByOfferId(offer.getId());
				typeOffer="BOX";
				photosProducts.add(box.getPhotoBoxPath());
				title = box.getTitle();
				description = box.getDescription();
			}

			Date orderDate = order.getCreatedAt() != null ? Date.from(order.getCreatedAt()) : null;
			String clientName = null;
			String clientProAvatar = null;
			String clientProActivity = null;
			OrderSource orderSource = null;
			if (order.getClient() != null) {
				clientName = order.getClient().getName().firstName() + " " + order.getClient().getName().lastName();
				orderSource = OrderSource.PRO_MARKET;
			} else {
				clientName = order.getClientPro().getName();
				clientProAvatar = order.getClientPro().getAvatarPath();
				clientProActivity = order.getClientPro().getActivities().get(0).getName();
				orderSource = OrderSource.DEAL_PRO;
			}
			boolean affected = false;
			if (order.getDelivery() != null) {
				affected = true;
			}

            SubEntityResponse subEntityResponse=null;
			SubEntity subEntity = subEntityRepository.findSubEntityByOrderId(order.getId());
			if (subEntity != null) {
				subEntityResponse = mapper.map(subEntity, SubEntityResponse.class);

			}


			return new OrderResponse(order.getId(),idDeal, order.getType(), order.getStatus(), orderSource, clientName,
					clientProAvatar, clientProActivity, typeOffer,photosProducts,barCode, title, description, null, orderDate,
					order.getOffer().getSalePrice(),offer.getPrice(),
					offerCreatorName,offerCreatorAvatar,order.isSeen(), affected);
		}, Order.class, OrderResponse.class);


	}
}