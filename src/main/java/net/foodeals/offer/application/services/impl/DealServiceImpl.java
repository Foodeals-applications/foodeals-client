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
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.offer.application.dtos.requests.DealDto;
import net.foodeals.offer.application.dtos.requests.DealProDto;
import net.foodeals.offer.application.dtos.requests.OpenTimeDto;
import net.foodeals.offer.application.dtos.requests.PriceBlockDTO;
import net.foodeals.offer.application.dtos.requests.RelaunchDealDto;
import net.foodeals.offer.application.dtos.responses.CartResponse;
import net.foodeals.offer.application.dtos.responses.DealProResponse;
import net.foodeals.offer.application.dtos.responses.ProductDealResponse;
import net.foodeals.offer.application.services.DealService;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.IOfferChoice;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.entities.OpenTime;
import net.foodeals.offer.domain.enums.DealStatus;
import net.foodeals.offer.domain.enums.OfferType;
import net.foodeals.offer.domain.exceptions.DealNotFoundException;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.offer.domain.repositories.OfferRepository;
import net.foodeals.offer.domain.repositories.OpenTimeRepository;
import net.foodeals.offer.domain.valueObject.Offerable;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.application.dtos.requests.SupplementDto;
import net.foodeals.product.application.services.ProductCategoryService;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.product.application.services.ProductSubCategoryService;
import net.foodeals.product.domain.entities.PriceBlock;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.entities.ProductSubCategory;
import net.foodeals.product.domain.entities.Supplement;
import net.foodeals.product.domain.repositories.SupplementRepository;
import net.foodeals.user.application.services.UserService;

@Service
@Transactional
@RequiredArgsConstructor
class DealServiceImpl implements DealService {

	private final DealRepository repository;
	private final OfferRepository offerRepository;
	private final OrderRepository orderRepository;
	private final OpenTimeRepository openTimeRepository;
	private final SupplementRepository supplementRepository;
	private final ProductService productService;
	private final ProductCategoryService categoryService;
	private final ProductSubCategoryService subCategoryService;
	private final UserService userService ;

	@Value("${upload.directory}")
	private String uploadDir;

	@Override
	public Deal findById(UUID id) {
		return repository.findById(id).orElseThrow(() -> new DealNotFoundException(id));
	}

	@Override
	public Deal create(DealDto dto) {

		Product product = null;
		List<Supplement> supplements = new ArrayList<>();
		if (!Objects.isNull(dto.supplements())) {
			supplements = dto.supplements().stream().map(
					supplementDto -> new Supplement(supplementDto.name(), supplementDto.price(), supplementDto.image()))
					.collect(Collectors.toList());

		} else {
			supplements = fetchSupplementsByIds(dto.supplementsIds());
		}
		if (dto.productId() != null) {
			product = productService.update(dto.productId(), dto.produtRequest());

		} else {
			product = productService.create(dto.produtRequest());
		}

		Price price = new Price(dto.price(), Currency.getInstance("MAD"));
		Price salePrice = new Price(dto.salePrice(), Currency.getInstance("MAD"));

		Offer offer = Offer.create(price, salePrice, dto.reduction(), product.getBarcode(),
				dto.openTimes().stream()
						.map(openTimeDto -> new OpenTime(openTimeDto.date(), openTimeDto.from(), openTimeDto.to()))
						.collect(Collectors.toList()),
				dto.modalityTypes(), dto.modalityPaiement(), dto.deliveryFee());

		final IOfferChoice offerChoice;

		Deal deal = Deal.create(price, dto.quantity(), product, supplements, dto.publishAs(), dto.category(), offer);
		deal.setDealStatus(DealStatus.AVAILABLE);
		deal.setDealUnityType(dto.dealUnityType());
		OrganizationEntity creator=userService.getConnectedUser().getOrganizationEntity();
		deal.setCreator(creator);
		deal = repository.save(deal);
		offerChoice = deal;
		offer.setOfferChoice(offerChoice);
		offer.setOfferable(new Offerable(offerChoice.getId(), OfferType.DEAL));
		offer = offerRepository.save(offer);
		List<OpenTime> opentimes = new ArrayList<>();
		for (OpenTimeDto openTimeDto : dto.openTimes()) {
			OpenTime openTime = new OpenTime(openTimeDto.date(), openTimeDto.from(), openTimeDto.to());
			openTime.setOffer(offer);
			openTime = openTimeRepository.save(openTime);
			opentimes.add(openTime);
		}
		offer.setOpenTime(opentimes);
		offerRepository.save(offer);
		return deal;

	}

	@Override
	public Deal update(UUID id, DealDto dto) {
		Deal deal = findById(id);
		Product product = productService.findById(deal.getProduct().getId());

		ProductRequest produtRequest = dto.produtRequest();
		if (product.getCategory() != null) {
			final ProductCategory category = categoryService.findById(product.getCategory().getId());
			product.setCategory(category);
		}
		if (product.getSubcategory() != null) {
			final ProductSubCategory subCategory = subCategoryService.findById(product.getSubcategory().getId());
			product.setSubcategory(subCategory);
		}
		product.setName(produtRequest.name());
		product.setTitle(produtRequest.title());
		product.setDescription(produtRequest.description());
		product.setProductImagePath(produtRequest.productImagePath());
		product.setBarcode(produtRequest.barcode());
		product.setType(produtRequest.type());
		product.setPrice(produtRequest.price());

		product.setBrand(produtRequest.brand());
		product.setRayon(produtRequest.rayon());

		List<Supplement> supplements = new ArrayList<>();

		for (SupplementDto supplementDto : dto.supplements()) {
			Supplement supplement = new Supplement(supplementDto.name(), supplementDto.price(), supplementDto.name());
			supplements.add(supplement);
		}

		deal.setDealUnityType(dto.dealUnityType());
		deal.setSupplements(supplements);
		deal.setQuantity(dto.quantity());
		deal.setPrice(new Price(dto.salePrice(), Currency.getInstance("MAD")));

		Offer offer = deal.getOffer();
		offer.setReduction(dto.reduction());
		offer.setModalityTypes(dto.modalityTypes());
		offer.setModalityPaiement(dto.modalityPaiement());
		offer.setDeliveryFee(dto.deliveryFee());

		List<OpenTime> opentimes = new ArrayList<>();
		for (OpenTimeDto openTimeDto : dto.openTimes()) {
			OpenTime openTime = new OpenTime(openTimeDto.date(), openTimeDto.from(), openTimeDto.to());
			opentimes.add(openTime);
		}
		offer.setOpenTime(opentimes);
		deal.setPublishAs(dto.publishAs());
		deal.setCategory(dto.category());

		offer = offerRepository.save(offer);

		deal.setOffer(offer);

		deal = repository.save(deal);
		return deal;
	}

	@Override
	public Page<Deal> findAll(Integer pageNumber, Integer pageSize) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public void delete(UUID id) {
		if (!repository.existsById(id))
			throw new DealNotFoundException(id);

		repository.softDelete(id);
	}

	@Override
	public List<Offer> findAllDealsOffers() {
		return repository.findAll().stream().map(Deal::getOffer).collect(Collectors.toList());
	}

	private List<Supplement> fetchSupplementsByIds(List<UUID> supplementIds) {
		return supplementIds.stream()
				.map(supplementId -> supplementRepository.findById(supplementId).orElseThrow(
						() -> new EntityNotFoundException("Supplement not found with id: " + supplementId)))
				.collect(Collectors.toList());
	}

	@Override
	public List<Offer> findAllHistoricsDealsOffers() {
		return repository.findAll().stream().map(Deal::getOffer).filter(offer -> isDealExpired(offer))
				.collect(Collectors.toList());
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

	private boolean isDealExpired(Offer offer) {
		if (offer != null && offer.getOpenTime() != null) {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			return offer.getOpenTime().stream().anyMatch(openTime -> {
				try {
					LocalDateTime endTime = LocalDateTime.parse(openTime.getTo(), formatter);
					return now.isAfter(endTime);
				} catch (Exception e) {

					return false;
				}
			});
		}
		return false;
	}

	@Override
	public List<Deal> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDeal(UUID id, String reason, String motif) {
		Deal deal = findById(id);
		if (!Objects.isNull(deal)) {
			deal.setReason(reason);
			deal.setMotif(motif);
			repository.save(deal);
			repository.softDelete(id);
		} else {
			throw new EntityNotFoundException();
		}

	}

	public Page<Deal> getExpiredAndUnavailableDeals(Pageable pageable) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
				.withZone(ZoneId.systemDefault());
		String formattedDateToday = formatter.format(Instant.now());

		return repository.findExpiredAndUnavailableDeals(DealStatus.UNVAVAILABLE, DealStatus.AVAILABLE,
				formattedDateToday, pageable);
	}

	@Override
	public Deal relaunchDeal(@Valid RelaunchDealDto dto, UUID id) {
		Deal deal = findById(id);
		deal.setDealStatus(DealStatus.AVAILABLE);
		Offer offer = deal.getOffer();
		offer.setModalityPaiement(dto.modalityPaiement());
		offer.setDeliveryFee(dto.deliveryFee());
		offer.setOpenTime(dto.openTimes().stream()
				.map(openTimeDto -> new OpenTime(openTimeDto.date(), openTimeDto.from(), openTimeDto.to()))
				.collect(Collectors.toList()));
		offer = offerRepository.save(offer);
		deal.setOffer(offer);
		return repository.save(deal);
	}

	@Override
	public Page<Deal> findDealPro(Pageable pageable) {

		return repository.findDealsWithProClients(pageable);
	}

	@Override
	public Deal createPro(DealProDto dto, String photoPath) {

		Product product = productService.create(dto.productRequest());
		product.setProductImagePath(photoPath);

		Offer offer = Offer.create(null, null, 0, product.getBarcode(), null, null, null, 0L);

		final IOfferChoice offerChoice;

		List<PriceBlock> defaultPrices = new ArrayList();
		if (dto.defaultPrices() != null) {
			for (PriceBlockDTO priceBlockDTO : dto.defaultPrices()) {
				PriceBlock priceBlock = new PriceBlock(priceBlockDTO.quantity(), priceBlockDTO.price());
				defaultPrices.add(priceBlock);
			}
		}

		List<PriceBlock> customPrices = new ArrayList();
		if (dto.customPrices() != null) {
			for (PriceBlockDTO priceBlockDTO : dto.customPrices()) {
				PriceBlock priceBlock = new PriceBlock(priceBlockDTO.quantity(), priceBlockDTO.price());
				customPrices.add(priceBlock);
			}
		}

		Deal dealPro = Deal.create(dto.quantity(), product, null, dto.publishAs(), dto.category(), offer,
				dto.dealUnityType(), defaultPrices, customPrices);
		dealPro.setProduct(product);
		dealPro.setDealStatus(DealStatus.AVAILABLE);
		OrganizationEntity creator=userService.getConnectedUser().getOrganizationEntity();
		dealPro.setCreator(creator);
		dealPro = repository.save(dealPro);
		offerChoice = dealPro;
		offer.setOfferChoice(offerChoice);
		offer.setOfferable(new Offerable(offerChoice.getId(), OfferType.DEAL));
		offer.setModalityTypes(dto.modalityTypes());
		offer.setModalityPaiement(dto.modalityPaiement());
		offer.setDeliveryFee(dto.deliveryFee());
		offerRepository.save(offer);

		return dealPro;
	}



}
