package net.foodeals.referals.infrastructure;

import lombok.RequiredArgsConstructor;
import net.foodeals.referals.application.dtos.responses.ReferralStatsResponse;
import net.foodeals.referals.application.service.ReferralService;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
