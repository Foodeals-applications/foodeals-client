package net.foodeals.offer.application.services;

import net.foodeals.core.domain.entities.Offer;
import net.foodeals.core.domain.entities.OpenTime;
import net.foodeals.offer.application.dtos.requests.OpenTimeDto;

public interface OpenTimeService {
    OpenTime create(OpenTimeDto dto, Offer offer);
}
