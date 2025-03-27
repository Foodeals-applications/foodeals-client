package net.foodeals.offer.domain.entities;

import java.util.UUID;

import net.foodeals.offer.domain.enums.DonorType;

public interface DonorInfo {
    UUID getId();

    DonorType getDonorType();
}