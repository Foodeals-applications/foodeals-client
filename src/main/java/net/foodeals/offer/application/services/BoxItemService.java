package net.foodeals.offer.application.services;

import net.foodeals.core.domain.entities.Box;
import net.foodeals.core.domain.entities.BoxItem;
import net.foodeals.offer.application.dtos.requests.BoxItemDto;

import java.util.List;

public interface BoxItemService {
    List<BoxItem> createAll(List<BoxItemDto> dtos, Box box);
}
