package net.foodeals.organizationEntity.application.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.requests.OrganizationEntityRequest;
import net.foodeals.organizationEntity.application.services.OrganizationEntityService;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.repositories.OrganizationEntityRepository;


@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationEntityServiceImpl implements OrganizationEntityService{

	
	private final OrganizationEntityRepository organizationEntityRepository;
	@Override
	public List<OrganizationEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<OrganizationEntity> findAll(Integer pageNumber, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganizationEntity findById(UUID id) {
		return organizationEntityRepository.findById(id).get();
	}

	@Override
	public OrganizationEntity create(OrganizationEntityRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganizationEntity update(UUID id, OrganizationEntityRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub
		
	}

}
