package net.foodeals.referals.application.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReferralInviteResultResponse {
    private int totalRequested;
    private int totalCreated;
    private int totalSkippedExisting;
    private List<ReferralResponse> createdReferrals;
}