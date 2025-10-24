package net.foodeals.dlc.application.dtos.responses;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ScanCreateResponse {
    private UUID id;
    private String userId;
    private UUID productId;
    private String name;
    private String barcode;
    private Integer quantity;
    private String unit;
    private String status;
    private Date createdAt;
    private Date updatedAt;
}