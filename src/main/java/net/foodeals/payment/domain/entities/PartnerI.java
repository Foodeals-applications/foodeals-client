package net.foodeals.payment.domain.entities;

import java.util.UUID;

import net.foodeals.payment.domain.entities.Enum.PartnerType;

public interface PartnerI {
    UUID getId();

    PartnerType getPartnerType();

    String getName();

    String getAvatarPath();

    boolean commissionPayedBySubEntities();
    boolean subscriptionPayedBySubEntities();
    boolean singleSubscription();

}