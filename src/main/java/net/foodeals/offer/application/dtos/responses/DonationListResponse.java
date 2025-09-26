package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.offer.domain.entities.Donate;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationListResponse {

    private  List<DonationResponse> donations;


}
