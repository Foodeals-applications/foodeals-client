package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.offer.domain.entities.Donate;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationResponse {

    private UUID id;
    private Double amount;
    private String cause;
    private boolean anonymous;
    private Instant createdAt;

    public static DonationResponse fromEntity(Donate donate) {
        DonationResponse dto = new DonationResponse();
        dto.setId(donate.getId());
        dto.setAmount(null);
        dto.setCause(donate.getReason()); // ou bien donate.getReason()
        dto.setAnonymous(donate.getIsAnonymous() == null ? false : donate.getIsAnonymous());
        dto.setCreatedAt(donate.getCreatedAt());
        return dto;
    }
}
