package net.foodeals.organizationEntity.application.dtos.responses;

import java.util.UUID;

import net.foodeals.location.application.dtos.responses.AddressResponse;
import net.foodeals.organizationEntity.domain.entities.enums.EntityType;


public record OrganizationEntityResponse (UUID id,String name,
   String avatarPath,
    String coverPath,
     EntityType type, AddressResponse address,
		String commercialNumber) {
}	

