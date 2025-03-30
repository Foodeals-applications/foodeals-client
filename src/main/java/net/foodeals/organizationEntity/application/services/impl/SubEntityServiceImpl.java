package net.foodeals.organizationEntity.application.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.location.domain.repositories.AddressRepository;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.CountryRepository;
import net.foodeals.location.domain.repositories.RegionRepository;
import net.foodeals.organizationEntity.application.dtos.requests.SubEntityRequest;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityStatus;
import net.foodeals.organizationEntity.domain.exceptions.SubEntityNotFoundException;
import net.foodeals.organizationEntity.domain.repositories.ActivityRepository;
import net.foodeals.organizationEntity.domain.repositories.OrganizationEntityRepository;
import net.foodeals.organizationEntity.domain.repositories.SolutionRepository;
import net.foodeals.organizationEntity.domain.repositories.SubEntityRepository;
import net.foodeals.product.domain.exceptions.ProductNotFoundException;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class SubEntityServiceImpl implements SubEntityService {

	private final SubEntityRepository repository;
	private final UserRepository userRepository;
	private final CityRepository cityRepository;
	private final CountryRepository countryRepository;
	private final RegionRepository regionRepository;
	private final AddressRepository addressRepository;
	private final ActivityRepository activityRepository;
	private final SolutionRepository solutionRepository;
	private final OrganizationEntityRepository organizationEntityRepository;
	private final ModelMapper mapper;

	@Value("${upload.directory}")
	private String uploadDir;

	@Override
	public List<SubEntity> findAll() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String email = userDetails.getUsername();
			User user = userRepository.findByEmail(email).get();
			return user.getOrganizationEntity().getSubEntities();
		}
		return null;
	}

	@Override
	public Page<SubEntity> findAll(Integer pageNumber, Integer pageSize) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public SubEntity findById(UUID id) {

		return repository.findById(id).orElse(null);
	}

	@Override
	public SubEntity create(SubEntityRequest request) {
		SubEntity subEntity = new SubEntity();
		subEntity.setName(request.name());
		subEntity.setAvatarPath(request.avatarPath());
		subEntity.setCoverPath(request.coverPath());
		subEntity.setIFrame(request.iFrame());
		subEntity.setPhone(request.phone());
		subEntity.setEmail(request.email());
		List<Solution> managedSolutions = fetchSolutionsByNames(request.solutionNames());
		subEntity.setSolutions(managedSolutions);

		List<Activity> activities = fetchActivitiesByNames(request.activiteNames());
		subEntity.setActivities(activities);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String email = userDetails.getUsername();
		User user = userRepository.findByEmail(email).get();

		OrganizationEntity organizationEntity = user.getOrganizationEntity();

		subEntity.setOrganizationEntity(organizationEntity);

		User manager = userRepository.findById(request.managerId())
				.orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + request.managerId()));
		subEntity.setManager(manager);

		Address address = new Address();
		address.setCountry(countryRepository.findById(request.countryId())
				.orElseThrow(() -> new EntityNotFoundException("Country not found with id: " + request.countryId())));
		address.setCity(cityRepository.findById(request.cityId())
				.orElseThrow(() -> new EntityNotFoundException("City not found with id: " + request.cityId())));
		address.setRegion(regionRepository.findById(request.regionId())
				.orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + request.regionId())));
		address.setExtraAddress(request.exactAdresse());

		address = addressRepository.save(address);
		subEntity.setAddress(address);
		subEntity.setSubEntityStatus(SubEntityStatus.INACTIVE);
		return repository.save(subEntity);
	}

	@Override
	public SubEntity update(UUID id, SubEntityRequest dto) {

		SubEntity subEntity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubEntity not found with id: " + id));

		subEntity.setName(dto.name());
		subEntity.setAvatarPath(dto.avatarPath());
		subEntity.setCoverPath(dto.coverPath());
		subEntity.setEmail(dto.email());
		subEntity.setPhone(dto.phone());
		subEntity.setIFrame(dto.iFrame());

		subEntity.setManager(userRepository.findById(dto.managerId())
				.orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + dto.managerId())));

		subEntity.setActivities(fetchActivitiesByNames(dto.activiteNames()));

		subEntity.setSolutions(fetchSolutionsByNames(dto.solutionNames()));

		Address address = addressRepository.findById(subEntity.getAddress().getId()).get();

		address.setCountry(countryRepository.findById(dto.countryId())
				.orElseThrow(() -> new EntityNotFoundException("Country not found with id: " + dto.countryId())));
		address.setCity(cityRepository.findById(dto.cityId())
				.orElseThrow(() -> new EntityNotFoundException("City not found with id: " + dto.cityId())));
		address.setRegion(regionRepository.findById(dto.regionId())
				.orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + dto.regionId())));

		subEntity.setAddress(address);

		return repository.save(subEntity);
	}

	@Override
	public SubEntity updateSubEntity(SubEntityRequest dto) {
		SubEntity subEntity = mapper.map(dto, SubEntity.class);
		return repository.save(subEntity);
	}

	@Override
	public void delete(UUID id) {
		if (!repository.existsById(id))
			throw new SubEntityNotFoundException(id);

		repository.softDelete(id);

	}

	private List<Solution> fetchSolutionsByNames(List<String> solutionNames) {
		if (solutionNames == null || solutionNames.isEmpty()) {
			throw new IllegalArgumentException("Solution names list cannot be null or empty.");
		}

		return solutionNames.stream().map(this::findSolutionByName).collect(Collectors.toList());
	}


	private Solution findSolutionByName(String solutionName) {
		return solutionRepository.findByName(solutionName);
	}


	private List<Activity> fetchActivitiesByNames(List<String> activityNames) {
		return activityNames.stream().map(activityName -> {
			Activity activity = activityRepository.findByName(activityName);
			if (activity == null) {
				throw new EntityNotFoundException("Activity not found with name: " + activityName);
			}
			return activity;
		}).collect(Collectors.toList());
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
	public void deleteSubEntity(UUID id, String reason, String motif) {

		SubEntity subEntity = findById(id);
		if (!Objects.isNull(subEntity)) {
			subEntity.setReason(reason);
			subEntity.setMotif(motif);
			subEntity.setSubEntityStatus(SubEntityStatus.ARCHIVED);
			repository.save(subEntity);
			repository.softDelete(id);
		} else {
			throw new ProductNotFoundException(id);
		}

	}

	public Page<SubEntity> filterSubEntities(Instant startDate, Instant endDate, String raisonSociale, UUID managerId,
			String email, String phone, UUID cityId, UUID solutionId, Pageable pageable) {
		return repository.filterSubEntities(raisonSociale, managerId, email, phone, cityId, solutionId,startDate,endDate, pageable);
	}

	@Override
	public Page<SubEntity> getAllByStatus(String status, Pageable pageable) {
        SubEntityStatus subEntityStatus=SubEntityStatus.valueOf(status);
		return repository.findAllBySubEntityStatus(subEntityStatus, pageable);
	}

	@Override
	public SubEntity confirmSubEntity(UUID id) {
		SubEntity subEntity=findById(id);
		subEntity.setSubEntityStatus(SubEntityStatus.ACTIVE);;
		return repository.save(subEntity);
	}
	
	
	@Override
	 public List<Map<String, Object>> getStoreCountByActivity() {
	        List<Object[]> results = repository.countStoresByActivity();
	        List<Map<String, Object>> response = new ArrayList();

	        for (Object[] result : results) {
	            Map<String, Object> activityMap = new HashMap();
	            activityMap.put("activity", result[0]);  // Nom de l'activité
	            activityMap.put("storeCount", result[1]); // Nombre de magasins
	            response.add(activityMap);
	        }

	        return response;
	    }

}
