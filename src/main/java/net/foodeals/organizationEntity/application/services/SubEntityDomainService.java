package net.foodeals.organizationEntity.application.services;

import net.foodeals.organizationEntity.application.dtos.responses.SubEntityDomainResponse;

import java.util.List;

public interface SubEntityDomainService {

    List<SubEntityDomainResponse> findAll();
}
