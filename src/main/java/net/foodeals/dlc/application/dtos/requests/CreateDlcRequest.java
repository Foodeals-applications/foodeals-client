package net.foodeals.dlc.application.dtos.requests;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CreateDlcRequest {
    private UUID productId;
    private Date expiryDate;
    private Integer quantity;
}
