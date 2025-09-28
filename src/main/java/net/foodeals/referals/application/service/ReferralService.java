package net.foodeals.referals.application.service;

import lombok.RequiredArgsConstructor;
import net.foodeals.notification.application.services.NotificationSettingsService;
import net.foodeals.referals.application.dtos.requests.ReferralInviteRequest;
import net.foodeals.referals.application.dtos.responses.ReferralInviteResultResponse;
import net.foodeals.referals.application.dtos.responses.ReferralResponse;
import net.foodeals.referals.application.dtos.responses.ReferralStatsResponse;
import net.foodeals.referals.domain.entities.Referral;
import net.foodeals.referals.domain.repositories.ReferralRepository;
import net.foodeals.user.domain.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferralService {

    private final ReferralRepository referralRepository;
    private final NotificationSettingsService notificationService;


    public ReferralStatsResponse getStats(User user) {
        List<Referral> referrals = referralRepository.findBySender(user);

        long totalInvites = referrals.size();
        long successfulReferrals = referrals.stream().filter(Referral::isSuccessful).count();
        double rewards = referrals.stream().filter(Referral::isSuccessful)
                .mapToDouble(Referral::getReward)
                .sum();

        return new ReferralStatsResponse(totalInvites, successfulReferrals, rewards);
    }

    public ReferralInviteResultResponse inviteFriends(User sender, ReferralInviteRequest request) {
        List<String> emails = request.getEmails() == null ? List.of() : request.getEmails();
        String message = request.getMessage() == null ? "" : request.getMessage();

        int totalRequested = emails.size();
        int totalCreated = 0;
        int totalSkipped = 0;
        List<ReferralResponse> created = new ArrayList<>();

        for (String email : emails) {
            if (email == null || email.isBlank()) {
                totalSkipped++;
                continue;
            }

            // Évite les doublons : si déjà invité par le même sender, on skip
            if (referralRepository.existsBySenderAndEmail(sender, email)) {
                totalSkipped++;
                continue;
            }

            Referral ref = new Referral();
            ref.setSender(sender);
            ref.setEmail(email.trim().toLowerCase());
            ref.setSuccessful(false); // invitation envoyée -> pas encore successful
            ref.setReward(0.0);
            Referral saved = referralRepository.save(ref);

            // Envoi de l'invitation (placeholder -> remplace par réel)
            notificationService.sendReferralInvitation(email, message, sender.getName().toString());

            ReferralResponse r = new ReferralResponse();
            r.setId(saved.getId());
            r.setEmail(saved.getEmail());
            r.setSuccessful(saved.isSuccessful());
            r.setReward(saved.getReward());
            r.setCreatedAt(saved.getCreatedAt());
            created.add(r);

            totalCreated++;
        }

        return new ReferralInviteResultResponse(totalRequested, totalCreated, totalSkipped, created);
    }

}