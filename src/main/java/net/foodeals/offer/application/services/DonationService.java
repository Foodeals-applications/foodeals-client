package net.foodeals.offer.application.services;

import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.Donate;
import net.foodeals.core.domain.entities.User;
import net.foodeals.core.domain.enums.DonateStatus;
import net.foodeals.core.repositories.DonateRepository;
import net.foodeals.core.repositories.UserRepository;
import net.foodeals.offer.application.dtos.requests.DonationRequest;
import net.foodeals.offer.application.dtos.responses.DonationListResponse;
import net.foodeals.offer.application.dtos.responses.DonationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonateRepository donateRepository;
    private final UserRepository userRepository;

    // Récupérer toutes les donations d’un utilisateur
    public DonationListResponse getUserDonations(User user) {
        List<Donate> donations = donateRepository.findByUserDonor(user);
        List<DonationResponse> donationResponses = donations.stream()
                .map(DonationResponse::fromEntity)
                .toList();
        return new DonationListResponse(donationResponses);
    }

    // Créer une nouvelle donation
    public DonationResponse createDonation(User user, DonationRequest request) {
        Donate donate = new Donate();
        donate.setUserDonor(user);
        donate.setReason(request.getCause());
        donate.setAmount(request.getAmount());
        donate.setIsAnonymous(request.isAnonymous());
        donate.setDonateStatus(DonateStatus.OPEN);

        Donate saved = donateRepository.save(donate);
        return DonationResponse.fromEntity(saved);
    }


}