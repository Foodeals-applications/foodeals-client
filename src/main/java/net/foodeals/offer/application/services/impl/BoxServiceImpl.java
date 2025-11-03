package net.foodeals.offer.application.services.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import net.foodeals.core.domain.entities.*;
import net.foodeals.core.domain.enums.BoxStatus;
import net.foodeals.core.domain.enums.BoxType;
import net.foodeals.core.domain.enums.Category;
import net.foodeals.core.domain.enums.OfferType;
import net.foodeals.core.exceptions.BoxNotFoundException;
import net.foodeals.core.repositories.*;
import net.foodeals.offer.application.dtos.responses.*;
import net.foodeals.product.application.dtos.responses.ProductOfferResponse;
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
import net.foodeals.common.Utils.DistanceCalculator;
import net.foodeals.offer.application.dtos.requests.BoxDto;
import net.foodeals.offer.application.dtos.requests.OpenTimeDto;
import net.foodeals.offer.application.services.BoxService;

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
    public BoxListResponse getBoxes(int page, int limit, String categoryStr) {
        PageRequest pageable = PageRequest.of(page - 1, limit);

        List<BoxResponse> boxResponses;
        long totalCount;

        if (!"all".equalsIgnoreCase(categoryStr)) {
            Category category = Category.fromString(categoryStr);
            if (category != null) {
                var pageResult = repository.findByCategory(category, pageable);
                boxResponses = pageResult.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();
                totalCount = pageResult.getTotalElements();
            } else {
                boxResponses = List.of();
                totalCount = 0;
            }
        } else {
            var pageResult = repository.findAll(pageable);
            boxResponses = pageResult.getContent()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
            totalCount = pageResult.getTotalElements();
        }

        boolean hasMore = page * limit < totalCount;

        return new BoxListResponse(boxResponses, totalCount, hasMore);
    }

    /**
     * Mapper d'une entité Box vers son DTO BoxResponse
     */
    private BoxResponse mapToResponse(Box box) {
        return new BoxResponse(
                box.getId().toString(),
                box.getTitle(),
                box.getDescription(),
                box.getOffer().getSalePrice().amount().doubleValue(),
                box.getPhotoBoxPath(),
                box.getOffer().getSubEntity().getId().toString(),
                box.getOffer().getSubEntity().getName(),
                box.getOffer().getSubEntity().getAvatarPath(),
                box.getProducts().stream().map(p->mapToProductResponse(p)).toList(),
                box.getBoxStatus().name(),
                box.getCategory().name()
        );
    }

    public ProductOfferResponse mapToProductResponse(Product product) {
        return new ProductOfferResponse(
                product.getId(),
                product.getProductImagePath(),      // adapte si ton champ dans Product s’appelle différemment
                product.getName(),
                product.getPrice().amount(),
                product.getPrice().amount(),
                product.getStock()
        );
    }

    @Override
    public List<BoxCategory> getAllCategories() {
        return Category.getCategoryPairs()
                .stream()
                .map(pair -> new BoxCategory(pair.getValue(), pair.getName()))
                .collect(Collectors.toList());
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

	@Override
	public BoxDetailsResponse getBoxDetails(UUID id) {
		Box box =findById(id);
		return mapBoxToBoxDetailsResponse(box);
	}

    @Override
    public FeaturedBoxesResponse getFeaturedBoxes() {
        // ⚡ Récupérer uniquement les boxes actives + mises en avant
        List<Box> featuredBoxes = repository.findByIsFeaturedTrueAndIsActiveTrue();

        List<BoxFeaturedResponse> responses = featuredBoxes.stream()
                .map(box -> new BoxFeaturedResponse(
                        box.getId(),
                        box.getTitle(),
                        box.getDescription(),
                        box.getOffer().getPrice().amount().doubleValue(),
                        box.getOffer().getSalePrice().amount().doubleValue(),
                        box.getOffer().getSubEntity().getName()
                ))
                .toList();

        return new FeaturedBoxesResponse(responses);
    }

    private BoxDetailsResponse mapBoxToBoxDetailsResponse(Box box) {
		 User user = userService.getConnectedUser();
	        BoxDetailsResponse response = new BoxDetailsResponse();
	        response.setId(box.getId());
	        response.setPhotoPath(box.getPhotoBoxPath());
	        response.setTitle(box.getTitle());
	        response.setQuantity(box.getQuantity());
	        response.setDescription(box.getDescription());
	        double distance = DistanceCalculator.calculateDistance(user.getCoordinates().latitude().doubleValue(),
	        		user.getCoordinates().longitude().doubleValue(),
	        	box.getOffer().getSubEntity().getCoordinates().latitude().doubleValue(),
	        	box.getOffer().getSubEntity().getCoordinates().latitude().doubleValue());
	        response.setNumberOfFeedback(box.getOffer().getNumberOfFeedBack());
	        response.setDistance(distance);
	        response.setNumberOfStars(box.getOffer().getNumberOfStars());
	        response.setReviews((response.getNumberOfFeedback() / response.getNumberOfStars()));
	        response.setEstimatedDeliveryTime(0f);
	        List<OpenTime> openTimes = box.getOffer().getOpenTime();
	        List<OpenTimeResponse> openTimeResponses = new ArrayList<>();
	        for (OpenTime openTime : openTimes) {
	            openTimeResponses.add(new OpenTimeResponse(openTime.getId(), openTime.getDate(), openTime.getFrom(), openTime.getTo()));
	        }
	        response.setOpenTime(openTimeResponses);
	        response.setModalityTypes(box.getOffer().getModalityTypes());
	        response.setCategoryName(null);
	        response.setAddress(box.getOffer().getSubEntity().getAddress().getAddress() + " "
	                + box.getOffer().getSubEntity().getAddress().getCity().getName());
	        response.setFavorite(user.getFavorisOffers().contains(box.getOffer()));

	        BigDecimal oldPrice = box.getOffer().getPrice().amount();
	        BigDecimal newPrice = box.getOffer().getSalePrice().amount();

	        response.setNewPrice(newPrice);
	        response.setOldPrice(oldPrice);

	        if (oldPrice != null && oldPrice.compareTo(BigDecimal.ZERO) > 0) {
	            BigDecimal discount = oldPrice.subtract(newPrice)
	                    .divide(oldPrice, 2, RoundingMode.HALF_UP) // pour obtenir un ratio avec 2 décimales
	                    .multiply(BigDecimal.valueOf(100));

	            response.setDiscount(discount.intValue()); // convertit en Integer
	        } else {
	            response.setDiscount(0);
	        }
	        
	
	        
	        List<UUID> productIds = box.getProducts()
                    .stream()
                    .map(Product::getId)
                    .toList();
	        List<Box>boxs=repository.findSimilarBoxesByProductIds(productIds, box.getId());
	        List<SimilarBoxResponse>similarDealResponses=boxs.stream().map(
	                b->new SimilarBoxResponse(b.getId(),
	                        b.getTitle(),
	                        b.getPhotoBoxPath(),
	                        b.getOffer().getSalePrice().amount(),
	                        b.getOffer().getPrice().amount())
	        ).collect(Collectors.toList());
	        response.setSimilarBoxResponses(similarDealResponses);
	        
	        Map<String, List<SupplementBoxResponse>> supplementResponses = box.getSupplements().stream()
	                .map(supplement -> new SupplementBoxResponse(
	                        supplement.getId(),
	                        supplement.getName(),
	                        supplement.getPrice(),
	                        supplement.getSupplementImagePath(),
	                        supplement.getSupplementCategory()
	                ))
	                .collect(Collectors.groupingBy(
	                        res -> res.supplementCategory().name(), // clé = nom de l'enum en String
	                        LinkedHashMap::new,                                // optionnel : garde l'ordre d'insertion
	                        Collectors.toList()
	                ));
	        response.setSupplementResponses(supplementResponses);
	      
	        response.setSupplementResponses(supplementResponses);

	        return response;

	}

}
