package net.foodeals.user.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatisticsResponse {
    private double savedAmount;    // argent économisé
    private int productsSaved;     // nb produits sauvés
    private double co2Saved;       // CO2 évité (kg)
}