package net.foodeals.location.application.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.delivery.application.services.impl.CoveredZonesService;
import net.foodeals.delivery.domain.entities.CoveredZones;
import net.foodeals.delivery.domain.repositories.CoveredZonesRepository;
import net.foodeals.organizationEntity.application.dtos.requests.CoveredZonesDto;
import net.foodeals.user.domain.exceptions.UserNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CoveredZonesServiceImpl implements CoveredZonesService {

	private final CoveredZonesRepository coveredZonesRepository;

	@Override
	public List<CoveredZones> findAll() {
		return coveredZonesRepository.findAll();
	}

	@Override
	public Page<CoveredZones> findAll(Integer pageNumber, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoveredZones findById(UUID id) {
		return coveredZonesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}

	@Override
	public CoveredZones create(CoveredZonesDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoveredZones update(UUID id, CoveredZonesDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub

	}

}
