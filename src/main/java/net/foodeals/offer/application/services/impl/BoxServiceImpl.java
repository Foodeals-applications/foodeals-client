package net.foodeals.offer.application.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.requests.BoxDto;
import net.foodeals.offer.application.dtos.requests.OpenTimeDto;
import net.foodeals.offer.application.services.BoxService;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.IOfferChoice;
import net.foodeals.offer.domain.entities.ModifiyHistory;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.entities.OpenTime;
import net.foodeals.offer.domain.entities.RelaunchHistory;
import net.foodeals.offer.domain.enums.BoxStatus;
import net.foodeals.offer.domain.enums.BoxType;
import net.foodeals.offer.domain.enums.OfferType;
import net.foodeals.offer.domain.exceptions.BoxNotFoundException;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.offer.domain.repositories.ModifiyHistoryRepository;
import net.foodeals.offer.domain.repositories.OfferRepository;
import net.foodeals.offer.domain.repositories.OpenTimeRepository;
import net.foodeals.offer.domain.repositories.RelaunchHistoryRepository;
import net.foodeals.offer.domain.valueObject.Offerable;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.user.application.services.UserService;

@Service
@Transactional
@RequiredArgsConstructor
class BoxServiceImpl implements BoxService {

	private final BoxRepository repository;
	private final ProductService productService;

	
	private final OpenTimeRepository openTimeRepository;
	private final OfferRepository offerRepository;
	private final RelaunchHistoryRepository relaunchHistoryRepository;
	private final ModifiyHistoryRepository modifiyHistoryRepository;
	private final UserService userService ;

	@Value("${upload.directory}")
	private String uploadDir;

	@Override
	public Box findById(UUID id) {
		return repository.findById(id).orElseThrow(() -> new BoxNotFoundException(id));
	}

	@Override
	public Box createBox(BoxDto dto, BoxType type) {

		Offer offer = Offer.create(dto.price(), null, dto.reduction(), null,
				dto.openTimes().stream()
						.map(openTimeDto -> new OpenTime(openTimeDto.date(), openTimeDto.from(), openTimeDto.to()))
						.collect(Collectors.toList()),
				dto.modalityTypes(), dto.modalityPaiement(), dto.deliveryFee());

		Box box = Box.create(dto.title(), dto.description(), dto.type(), dto.publishAs(), dto.category(), offer);
		box.setType(type);
		box.setQuantity(dto.quantity());
		box.setPhotoBoxPath(dto.photoBoxPath());
		box.setBoxStatus(BoxStatus.AVAILABLE);
		box = repository.save(box);

		final IOfferChoice offerChoice;
		offerChoice = box;
		offer.setOfferChoice(offerChoice);
		offer.setOfferable(new Offerable(offerChoice.getId(), OfferType.BOX));
		offerRepository.save(offer);
		return box;

	}

	@Override
	public void delete(UUID id) {
		if (!repository.existsById(id))
			throw new BoxNotFoundException(id);

		repository.softDelete(id);
	}

	public List<Offer> findOffersByBoxType(BoxType boxType) {

		return repository.findByType(boxType).stream().filter(box -> box.getDeletedAt() == null).map(Box::getOffer)
				.collect(Collectors.toList());
	}

	@Override
	public List<Box> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Box> findAllNormalBox(Integer pageNumber, Integer pageSize) {
		List<Box> boxes = repository.findByType(BoxType.NORMAL_BOX);
		List<Box> filteredBoxes = boxes.stream().filter(box -> box.getDeletedAt() == null).collect(Collectors.toList());
		return new PageImpl(filteredBoxes, PageRequest.of(pageNumber, pageSize), boxes.size());
	}

	@Override
	public Page<Box> findAllSurpriseBox(Integer pageNumber, Integer pageSize) {
		List<Box> boxes = repository.findByType(BoxType.MYSTERY_BOX);
		List<Box> filteredBoxes = boxes.stream().filter(box -> box.getDeletedAt() == null).collect(Collectors.toList());
		return new PageImpl(filteredBoxes, PageRequest.of(pageNumber, pageSize), boxes.size());
	}

	@Override
	public Box update(UUID id, BoxDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Box> findAll(Integer pageNumber, Integer pageSize) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public String saveFile(MultipartFile file) {

		Path path = Paths.get(uploadDir, file.getOriginalFilename());
		try {
			Files.createDirectories(path.getParent());
			Files.write(path, file.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Problem while saving the file.", e);
		}

		return file.getOriginalFilename();
	}

	@Override
	public void deleteBox(UUID id, String reason, String motif) {
		Box box = findById(id);
		if (!Objects.isNull(box)) {
			box.setReason(reason);
			box.setMotif(motif);
			repository.save(box);
			repository.softDelete(id);
		} else {
			throw new EntityNotFoundException();
		}

	}

	public Page<Box> getExpiredAndUnavailableBoxs(Pageable pageable, BoxType type) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
				.withZone(ZoneId.systemDefault());
		String formattedDateToday = formatter.format(Instant.now());

		return repository.findExpiredAndUnavailableBoxs(BoxStatus.UNVAVAILABLE, BoxStatus.AVAILABLE, type,
				formattedDateToday, pageable);
	}

	@Override
	public Box findByIdAndType(UUID id, BoxType typeBox) {
		return repository.findByIdAndType(id, typeBox).get();
	}

	@Override
	public Box create(BoxDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Box updateBox(UUID id, BoxDto dto, BoxType normalBox) {
		Box box = findById(id);

		Offer offer = box.getOffer();
		offer.setPrice(dto.price());
		offer.setReduction(dto.reduction());
		offer.setModalityPaiement(dto.modalityPaiement());
		offer.setModalityTypes(dto.modalityTypes());
		offer.setDeliveryFee(dto.deliveryFee());

		List<OpenTime> opentimes = new ArrayList<>();
		for (OpenTimeDto openTimeDto : dto.openTimes()) {
			OpenTime openTime = new OpenTime(openTimeDto.date(), openTimeDto.from(), openTimeDto.to());
			openTime.setOffer(offer);
			openTime = openTimeRepository.save(openTime);
			opentimes.add(openTime);
		}
		offer.setOpenTime(opentimes);

		box.setTitle(dto.title());
		box.setDescription(dto.description());
		box.setPublishAs(dto.publishAs());
		box.setType(normalBox);
		box.setBoxStatus(BoxStatus.AVAILABLE);
		box.setPhotoBoxPath(dto.photoBoxPath());
		box = repository.save(box);
		offerRepository.save(offer);
		
		ModifiyHistory modifiyHistory = new ModifiyHistory(userService.getConnectedUser().getOrganizationEntity(), box, LocalDateTime.now());
        modifiyHistoryRepository.save(modifiyHistory);
		return box;
	}

	@Override
	public Box relaunchBox(UUID id) {
		Box box = findById(id);
		
			box.setBoxStatus(BoxStatus.AVAILABLE);
			RelaunchHistory relaunchHistory = new RelaunchHistory(userService.getConnectedUser().getOrganizationEntity(), box, LocalDateTime.now());
	        relaunchHistoryRepository.save(relaunchHistory);  
			return repository.save(box);
		

	}

}
