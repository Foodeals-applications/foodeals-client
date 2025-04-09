package net.foodeals.delivery.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class DeliveryResponse {
    private Boolean available;         // Disponibilité de la livraison
    private String timeToDeliver;      // Délai estimé pour la livraison
    private List<String> pickupHours;  // Heures possibles pour le retrait
    private String createAddressUrl;   // URL pour créer une nouvelle adresse
}
