package net.foodeals.offer.application.services;

import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.dtos.requests.DonationRequest;
import net.foodeals.offer.application.dtos.responses.DonationListResponse;
import net.foodeals.offer.application.dtos.responses.DonationResponse;
import net.foodeals.offer.domain.entities.Donate;
import net.foodeals.offer.domain.enums.DonateStatus;
import net.foodeals.offer.domain.repositories.DonateRepository;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonateRepository donateRepository;
    private final UserRepository userRepository;

    public DonationListResponse getUserDonations(User user) {
        List<Donate> donations = donateRepository.findByUserDonor(user);
        return new DonationListResponse(getDonations(donations));
    }

    public DonationResponse createDonation(User user, DonationRequest request) {


        Donate donate = new Donate();
        donate.setUserDonor(user);
        donate.setMotif(request.getCause());
        donate.setIsAnonymous(request.isAnonymous());
        donate.setDonateStatus(DonateStatus.OPEN);

        Donate saved = donateRepository.save(donate);
        return DonationResponse.fromEntity(saved);
    }


    public List<DonationResponse> getDonations(List<Donate> entities) {
        List<DonationResponse> donations = entities.stream()
                .map(DonationResponse::fromEntity)
                .toList();
        return donations;
    }
}