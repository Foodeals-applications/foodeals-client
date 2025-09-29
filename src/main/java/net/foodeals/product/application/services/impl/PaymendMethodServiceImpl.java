package net.foodeals.product.application.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.product.application.dtos.requests.PaymentMethodRequest;
import net.foodeals.product.application.dtos.responses.PaymentMethodResponse;
import net.foodeals.product.application.services.PaymentMethodService;
import net.foodeals.product.domain.entities.PaymentMethodProduct;
import net.foodeals.product.domain.repositories.PaymentMethodProductRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymendMethodServiceImpl implements PaymentMethodService {

	private final PaymentMethodProductRepository paymentMethodRepository;
	private final ModelMapper mapper;

	@Override
	public List<PaymentMethodResponse> findAll() {
		List<PaymentMethodProduct> paymentMethodProducts = paymentMethodRepository.findAll();
		return paymentMethodProducts.stream().map(paymentMethod -> mapper.map(paymentMethod, PaymentMethodResponse.class))
				.collect(Collectors.toList());

	}

	@Override
	public Page<PaymentMethodResponse> findAll(Integer pageNumber, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentMethodResponse findById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentMethodResponse create(PaymentMethodRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentMethodResponse update(UUID id, PaymentMethodRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub

	}

}
