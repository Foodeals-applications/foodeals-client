package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.offer.domain.entities.Donate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationResponse {

    private String id;
    private String title;
    private String image;
    private Double price;
    private String currency;
    private String refText;
    private String association;
    private String dateText;

    public static DonationResponse fromEntity(Donate donate) {
        DonationResponse dto = new DonationResponse();
        dto.setId(donate.getId().toString());
        dto.setTitle(donate.getReason()); // ou le champ qui correspond au titre/cause
        dto.setImage(null); // si tu n’as pas d’image pour l’instant
        dto.setPrice(donate.getAmount()); // map le montant
        dto.setCurrency("EUR"); // ou ton currency par défaut
        dto.setRefText("REF-" + donate.getId().toString().substring(0, 8)); // exemple de ref
        dto.setAssociation(donate.getUserDonor() != null ? donate.getUserDonor().getName().firstName()+" "
                +donate.getUserDonor().getName().lastName(): null); // nom de l’association si dispo
        // formatage de la date en texte lisible
        dto.setDateText(DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .withZone(ZoneId.systemDefault())
                .format(donate.getCreatedAt() != null ? donate.getCreatedAt() : Instant.now()));
        return dto;
    }
}
