package net.foodeals.offer.application.dtos.responses;

import net.foodeals.core.domain.entities.Price;
import net.foodeals.core.domain.enums.SupplementCategory;

import java.util.UUID;


public record SupplementBoxResponse (UUID id , String name, Price price, String image, SupplementCategory supplementCategory) {

}
