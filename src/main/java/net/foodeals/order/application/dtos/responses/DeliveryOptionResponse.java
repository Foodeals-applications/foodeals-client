package net.foodeals.order.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOptionResponse {
    private String type;       // e.g. "HOME", "PICKUP"
    private String label;      // e.g. "Home Delivery", "Pickup Point"
    private Double cost;       // e.g. 20.0
    private String currency;   // e.g. "MAD"
    private String estimatedTime; // e.g. "30-45 min"
}