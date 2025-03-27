package net.foodeals.offer.application.dtos.requests;

import java.util.Date;

public record OpenTimeDto(
        Date date, String from, String to
) {
}
