package net.foodeals.dlc.application.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.dlc.application.dtos.requests.DlcRequest;
import net.foodeals.dlc.application.dtos.responses.DlcDetailsResponse;
import net.foodeals.dlc.application.dtos.responses.DlcResponse;
import net.foodeals.dlc.application.services.DlcService;
import net.foodeals.dlc.domain.entities.Dlc;
import net.foodeals.dlc.domain.entities.Modification;
import net.foodeals.dlc.domain.exceptions.DlcNotFoundException;
import net.foodeals.dlc.domain.repositories.DlcRepository;
import net.foodeals.dlc.domain.repositories.ModificationRepository;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.user.application.dtos.responses.UserResponse;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

@Service
@Transactional
@RequiredArgsConstructor
public class DlcServiceImpl implements DlcService {

	private final DlcRepository dlcRepository;
	private final ModelMapper mapper;
	private final ProductService productService;
	private final ModificationRepository modificationRepository;
	private final UserService userService;

	@Override
	public Page<Dlc> findAll(Integer pageNumber, Integer pageSize) {
		return dlcRepository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public Dlc findById(UUID id) {
		return dlcRepository.findById(id).orElseThrow(() -> new DlcNotFoundException(id));
	}

	@Override
	public Dlc create(DlcRequest dto) {
		Product product = productService.findById(dto.productId());
		Dlc dlc = Dlc.create(product, dto.expiryDate(), dto.quantity());
		return dlcRepository.save(dlc);
	}

	@Override
	public Dlc update(UUID id, DlcRequest dto) {
		Dlc dlc = findById(id);
		User currentUser = userService.getConnectedUser();

		int previousQuantity = dlc.getQuantity();
		int previousDiscount = dlc.getDiscount();

		dlc.getUsers().add(currentUser);
		dlc.setQuantity(dto.quantity());
		dlc.setExpiryDate(dto.expiryDate());

		Dlc updatedDlc = dlcRepository.save(dlc);

		Modification modification = Modification.create(dlc, currentUser, previousQuantity, updatedDlc.getQuantity(),
				previousDiscount, updatedDlc.getDiscount());
		modificationRepository.save(modification);

		return updatedDlc;
	}

	@Override
	public void delete(UUID id) {
		Dlc dlc = findById(id);
		dlcRepository.delete(dlc);
	}

	@Override
	public DlcDetailsResponse findDlcDetails(UUID id) {
		/*Dlc dlc = findById(id);

		List<Modification> modifications = modificationRepository.findByDlcId(id);
		List<UserResponse> users = userService.findByDlcId(id).stream()
				.map(user -> mapper.map(user, UserResponse.class)).collect(Collectors.toList());

		return new DlcDetailsResponse(mapper.map(dlc, DlcResponse.class), modifications.size(), users.size(), users);
	 */
		return null;
	}

	@Override
	public Dlc applyDiscount(UUID dlcId, int discountPercentage) {
		Dlc dlc = findById(dlcId);
		User currentUser = userService.getConnectedUser();

		int previousQuantity = dlc.getQuantity();
		int previousDiscount = dlc.getDiscount();

		dlc.setDiscount(discountPercentage);
		dlc.getUsers().add(currentUser);

		Product updatedProduct = productService.applyDiscount(dlc.getProduct().getId(), discountPercentage);
		dlc.setProduct(updatedProduct);

		Dlc updatedDlc = dlcRepository.save(dlc);

		Modification modification = Modification.create(dlc, currentUser, previousQuantity, updatedDlc.getQuantity(),
				previousDiscount, updatedDlc.getDiscount());
		modificationRepository.save(modification);

		return updatedDlc;
	}

	@Override
	public List<Dlc> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
}
