package net.foodeals.businessrecomendations.application.service;

import lombok.RequiredArgsConstructor;
import net.foodeals.businessrecomendations.application.dtos.requests.BusinessRegisterRequest;
import net.foodeals.businessrecomendations.application.dtos.responses.BusinessRegisterResponse;
import net.foodeals.businessrecomendations.domain.entities.BusinessApplication;
import net.foodeals.businessrecomendations.domain.enums.ApplicationStatus;
import net.foodeals.businessrecomendations.domain.repositories.BusinessApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BusinessApplicationService {

    private final BusinessApplicationRepository repository;

    public BusinessRegisterResponse registerBusiness(BusinessRegisterRequest request) {
        BusinessApplication application = BusinessApplication.builder()
                .businessName(request.getBusinessName())
                .ownerName(request.getOwnerName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .category(request.getCategory())
                .status(ApplicationStatus.PENDING_REVIEW)
                .submittedAt(Instant.now())
                .build();

        BusinessApplication saved = repository.save(application);

        return BusinessRegisterResponse.builder()
                .id(saved.getId())
                .businessName(saved.getBusinessName())
                .status(saved.getStatus().name())
                .submittedAt(saved.getSubmittedAt())
                .message("Votre demande d'enregistrement a été reçue et sera examinée sous 48h.")
                .build();
    }}