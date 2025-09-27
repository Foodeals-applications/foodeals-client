package net.foodeals.referals.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferralStatsResponse {
    private long totalInvites;
    private long successfulReferrals;
    private double rewards;
}