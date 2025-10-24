package net.foodeals.dlc.application.dtos.requests;


import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CreateUserProductRequest {
    private UUID productId; // optional
    private Date expiresAt;
    private Integer quantity;
    private String name; // for manual product create
}
