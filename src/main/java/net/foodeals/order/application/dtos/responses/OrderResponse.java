package net.foodeals.order.application.dtos.responses;

import java.time.Instant;
import java.util.UUID;


import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderResponse {
    private UUID id;                // ID de la commande
    private String productName;        // Nom du produit
    private String productImage;       // URL de l'image du produit
    private Double price;              // Prix du produit
    private Instant orderDate;         // Date de la commande
    private int  collectionStartTime;
    private int  collectionEndTime;
    private String transactionRef;     // Référence de la transaction


}
