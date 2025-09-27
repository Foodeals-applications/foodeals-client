package net.foodeals.referals.application.service;

import lombok.RequiredArgsConstructor;
import net.foodeals.referals.application.dtos.responses.ReferralStatsResponse;
import net.foodeals.referals.domain.entities.Referral;
import net.foodeals.referals.domain.repositories.ReferralRepository;
import net.foodeals.user.domain.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferralService {

    private final ReferralRepository referralRepository;

    public ReferralStatsResponse getStats(User user) {
        List<Referral> referrals = referralRepository.findBySender(user);

        long totalInvites = referrals.size();
        long successfulReferrals = referrals.stream().filter(Referral::isSuccessful).count();
        double rewards = referrals.stream().filter(Referral::isSuccessful)
                .mapToDouble(Referral::getReward)
                .sum();

        return new ReferralStatsResponse(totalInvites, successfulReferrals, rewards);
    }}