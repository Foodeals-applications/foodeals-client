package net.foodeals.organizationEntity.application.dtos.responses;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubEntityResponse {
	private String name;               // Nom du magasin
	private String address;            // Adresse du magasin
	private String distance;           // Distance (par exemple, "5 km")
	private Double donationsToAssociations; // Montant donné aux associations (exemple : "5000€")
	private Integer antiWasteYears;    // Années de lutte anti-gaspillage
	private Integer basketsSold;

}
