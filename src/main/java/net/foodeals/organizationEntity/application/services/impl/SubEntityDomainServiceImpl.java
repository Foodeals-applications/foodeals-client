package net.foodeals.organizationEntity.application.services.impl;

import lombok.AllArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityDomainResponse;
import net.foodeals.organizationEntity.application.services.SubEntityDomainService;
import net.foodeals.organizationEntity.domain.entities.SubEntityDomain;
import net.foodeals.organizationEntity.domain.repositories.SubEntityDomainRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class SubEntityDomainServiceImpl implements SubEntityDomainService {

    private final SubEntityDomainRepository subEntityDomainRepository;
    @Override
    public List<SubEntityDomainResponse> findAll() {
        List<SubEntityDomain> subEntityDomains = subEntityDomainRepository.findAll();
        List<SubEntityDomainResponse> subEntityDomainResponses = new ArrayList<>();
        subEntityDomains.stream().map(subEntityDomain -> new SubEntityDomainResponse(subEntityDomain.getId(),subEntityDomain.getName())).forEach(subEntityDomainResponses::add);
        return subEntityDomainResponses;
    }
}
