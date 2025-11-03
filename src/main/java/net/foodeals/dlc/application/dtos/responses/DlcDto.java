package net.foodeals.dlc.application.dtos.responses;

import lombok.Data;
import net.foodeals.core.domain.enums.ValorisationType;

import java.util.Date;
import java.util.UUID;

@Data
public class DlcDto {
    private UUID id;
    private String productName;
    private Date expiryDate;
    private String timeRemaining;
    private Integer quantity;
    private Integer discount;
    private ValorisationType valorisationType;
}