package net.foodeals.delivery.interfaces.web.modelMapperConfig;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.delivery.domain.entities.Delivery;
import net.foodeals.order.application.dtos.responses.DeliveryResponse;

@Configuration
@RequiredArgsConstructor
@Transactional
public class DeliveryModelMapperConfig {

	private final ModelMapper mapper;

	@PostConstruct
	public void configure() {

		mapper.addConverter(context -> {
			Delivery delivery = context.getSource();
			return new DeliveryResponse(delivery.getId(),
					delivery.getOrder().getClient().getName().firstName() + " "
							+ delivery.getOrder().getClient().getName().lastName(),
					delivery.getOrder().getClient().getPhone(), delivery.getStatus(), delivery.getDuration());
		}, Delivery.class, DeliveryResponse.class);

	}

}
