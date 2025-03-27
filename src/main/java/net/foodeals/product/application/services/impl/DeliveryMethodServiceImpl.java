package net.foodeals.product.application.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.requests.DeliveryMethodRequest;
import net.foodeals.product.application.dtos.responses.DeliveryMethodResponse;
import net.foodeals.product.application.services.DeliveryMethodService;
import net.foodeals.product.domain.entities.DeliveryMethod;
import net.foodeals.product.domain.repositories.DeliveryMethodRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryMethodServiceImpl implements DeliveryMethodService {

	private final DeliveryMethodRepository deliveryMethodRepository;
	private final ModelMapper mapper;

	@Override
	public List<DeliveryMethodResponse> findAll() {
		List<DeliveryMethod> deliveryMethods = deliveryMethodRepository.findAll();
		return deliveryMethods.stream().map(deliveryMethod -> mapper.map(deliveryMethod, DeliveryMethodResponse.class))
				.collect(Collectors.toList());

	}

	@Override
	public Page<DeliveryMethodResponse> findAll(Integer pageNumber, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeliveryMethodResponse findById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeliveryMethodResponse create(DeliveryMethodRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeliveryMethodResponse update(UUID id, DeliveryMethodRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub

	}

}