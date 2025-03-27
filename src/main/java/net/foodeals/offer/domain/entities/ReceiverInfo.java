package net.foodeals.offer.domain.entities;

import java.util.UUID;

import net.foodeals.offer.domain.enums.DonationReceiverType;

public interface ReceiverInfo {
    UUID getId();

    DonationReceiverType getReceiverType();
}