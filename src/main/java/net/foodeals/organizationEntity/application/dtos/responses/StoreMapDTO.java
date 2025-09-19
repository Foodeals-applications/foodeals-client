package net.foodeals.organizationEntity.application.dtos.responses;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreMapDTO {
    private UUID id;
    private String name;
    private String logo; // avatarPath
    private float latitude;
    private float longitude;
}