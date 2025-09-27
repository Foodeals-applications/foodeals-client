package net.foodeals.offer.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationRequest {
    private String cause;
    private Double amount;
    private boolean anonymous;
}