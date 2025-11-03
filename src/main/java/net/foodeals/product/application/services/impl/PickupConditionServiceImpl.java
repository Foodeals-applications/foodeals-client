package net.foodeals.product.application.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import net.foodeals.core.domain.entities.PickupCondition;
import net.foodeals.core.repositories.PickupConditionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.requests.PickupConditionRequest;
import net.foodeals.product.application.dtos.responses.PickupConditionResponse;
import net.foodeals.product.application.services.PickupConditionService;

@Service
@Transactional
@RequiredArgsConstructor
public class PickupConditionServiceImpl implements PickupConditionService {

	private final PickupConditionRepository repository;
	private final ModelMapper mapper;

	@Override
	public List<PickupConditionResponse> findAll() {
		List<PickupCondition> pickupConditions = repository.findAll();
		return pickupConditions.stream().map(pickupCondition -> mapper.map(pickupCondition, PickupConditionResponse.class))
				.collect(Collectors.toList());

	}

	@Override
	public Page<PickupConditionResponse> findAll(Integer pageNumber, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PickupConditionResponse findById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PickupConditionResponse create(PickupConditionRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PickupConditionResponse update(UUID id, PickupConditionRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub

	}

}
