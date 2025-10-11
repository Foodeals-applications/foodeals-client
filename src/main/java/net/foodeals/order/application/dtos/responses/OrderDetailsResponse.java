package net.foodeals.order.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.offer.domain.enums.ModalityPaiement;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDetailsResponse {

    private UUID id;

    private String productName;

    private String productDescription;

    private String productPhoto;

    private double price ;

    private Date deliveryOrCollectionDate;

    private int hourOfCollectionOrDelivey;

    private ModalityPaiement modalityPaiement;

    private UUID dealId ;

    private String qrCode;
}
