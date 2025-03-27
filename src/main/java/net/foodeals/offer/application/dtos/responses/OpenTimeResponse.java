package net.foodeals.offer.application.dtos.responses;

import java.util.Date;
import java.util.UUID;

public record OpenTimeResponse(
        UUID id,
        Date date,
        String from,
        String to
) {}
