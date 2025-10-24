package net.foodeals.dlc.application.dtos.requests;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateDlcRequest {
    private Date expiryDate;
    private Integer quantity;
    private String reason;
}