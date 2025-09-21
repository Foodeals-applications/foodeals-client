package net.foodeals.offer.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoxCategory {
    private String value; // nom de l'enum
    private String name;  // label lisible
}