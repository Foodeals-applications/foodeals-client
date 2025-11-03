package net.foodeals.referals.infrastructure;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.core.domain.entities.User;
import net.foodeals.referals.application.dtos.requests.ReferralInviteRequest;
import net.foodeals.referals.application.dtos.responses.ReferralInviteResultResponse;
import net.foodeals.referals.application.dtos.responses.ReferralStatsResponse;
import net.foodeals.referals.application.service.ReferralService;
import net.foodeals.user.application.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/referrals")
@RequiredArgsConstructor
public class ReferralController {

    private final ReferralService referralService;
    private final UserService userService;

    @GetMapping("/stats")
    public ReferralStatsResponse
    getStats() {
        User user = userService.getConnectedUser();
        return referralService.getStats(user);
    }

    @PostMapping("/invite")
    public ReferralInviteResultResponse invite(@RequestBody ReferralInviteRequest request) {

        User user = userService.getConnectedUser();
        return referralService.inviteFriends(user, request);
    }
}
