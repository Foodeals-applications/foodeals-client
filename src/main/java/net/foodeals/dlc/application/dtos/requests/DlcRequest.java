package net.foodeals.dlc.application.dtos.requests;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record DlcRequest (UUID productId, @NotNull Date expiryDate, @NotNull Integer quantity) {

}
