package net.foodeals.order.infrastructure.modelMapperConfig;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.BoxItem;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.enums.ModalityType;
import net.foodeals.offer.domain.enums.OfferType;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.order.application.dtos.responses.OrderDetailsResponse;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.enums.TransactionType;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.UserRepository;
import net.foodeals.user.domain.valueObjects.Name;

@Configuration
@RequiredArgsConstructor
@Transactional
public class OrderDetailsModelMapperConfig {

	private final ModelMapper mapper;
	private final DealRepository dealRepository;
	private final BoxRepository boxRepository;
	private final UserRepository userRepository;

	@PostConstruct
	public void configure() {

		mapper.addConverter(context -> {
			Order order = context.getSource();
			Integer quantity = null;
			List<String> photosProducts = new ArrayList<>();
			String title = null;
			String description = null;
			String sellerName = null;
			String sellerAvatar = null;
			Offer offer = order.getOffer();
			String avatarClient=null;
			String clientActivity=null;
			String sellerActivity=null;
			String sellerContact=null;
			if (offer.getOfferable().type().equals(OfferType.DEAL)) {
				Deal deal = dealRepository.getDealByOfferId(offer.getId());
				Product product = deal.getProduct();
				photosProducts.add(product.getProductImagePath());
				title = product.getTitle();
				description = product.getDescription();
				quantity = deal.getQuantity();
				sellerName = deal.getCreator().getName();
				sellerAvatar = deal.getCreator().getAvatarPath();
				sellerActivity=deal.getCreator().getMainActivity().getName();
              //  User contact=userRepository.findManagerOfOrganizationEntity(deal.getCreator().getId());
               // sellerContact=contact.getName().firstName()+ " "+contact.getName().lastName();
			
			} else {
				Box box = boxRepository.getBoxByOfferId(offer.getId());
				title = box.getTitle();
				description = box.getDescription();

				quantity = box.getQuantity();
			}

			Date orderDate = order.getCreatedAt() != null ? Date.from(order.getCreatedAt()) : null;

			LocalTime hourOfOrder = order.getCreatedAt() != null
					? order.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalTime()
					: null;

			String typePayment = null;
			if (order.getTransaction().getType().equals(TransactionType.CARD)) {
				typePayment = "CARD";
			} else {
				typePayment = "CASH";
			}
			Name deliveryBoyName = null;
			String deliveryBoyPhone = null;
			String deliveryBoyEmail = null;
			Date dateOfDelivery = null;
			LocalTime hourOfDelivery = null;
			if (order.getDelivery() != null) {
				deliveryBoyName = order.getDelivery().getDeliveryBoy().getName();
				deliveryBoyPhone = order.getDelivery().getDeliveryBoy().getPhone();
				deliveryBoyEmail = order.getDelivery().getDeliveryBoy().getEmail();
				dateOfDelivery = order.getDelivery() != null ? Date.from(order.getDelivery().getCreatedAt()) : null;

				hourOfOrder = order.getDelivery().getCreatedAt() != null
						? order.getDelivery().getCreatedAt().atZone(ZoneId.systemDefault()).toLocalTime()
						: null;

			}

			List<ModalityType> types = null;
			if (order.getOffer().getModalityTypes() == null) {
				types = List.of(ModalityType.DELIVERY);
			}

			if(order.getClient()!=null) {
				avatarClient=order.getClient().getAvatarPath();
			}
			
			if(order.getClientPro()!=null) {
				avatarClient=order.getClientPro().getAvatarPath();
				clientActivity=order.getClientPro().getActivities().get(0).getName();
			}
			return new OrderDetailsResponse(order.getId(), quantity, orderDate, hourOfOrder, photosProducts, title,
					description, offer.getPrice(), order.getQuantityOfOrder(), 
					order.getClient().getName(),avatarClient,
					order.getClient().getPhone(), order.getClient().getEmail(),clientActivity,
					sellerName, sellerAvatar, sellerActivity,sellerContact,typePayment,
					null, dateOfDelivery, hourOfDelivery, deliveryBoyName, deliveryBoyPhone, deliveryBoyEmail,
					order.getShippingAddress().getAddress() + order.getShippingAddress().getCity().getName() + " "
							+ order.getShippingAddress().getRegion().getName(),
					order.getStatus(), order.getCancellationReason(), order.getCancellationSubject(),
					order.getAttachmentPath(), types);
		}, Order.class, OrderDetailsResponse.class);

	}
}