package net.foodeals.offer.application.dtos.responses;

import java.util.UUID;

import net.foodeals.common.valueOjects.Price;
import net.foodeals.product.domain.enums.SupplementCategory;

public record SupplementDealResponse (UUID id , String name, Price price, String image, SupplementCategory supplementCategory) {

}
