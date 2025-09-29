package net.foodeals.order.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.order.domain.entities.PaymentMethod;
import net.foodeals.product.domain.entities.PaymentMethodProduct;

import java.util.Optional;
import java.util.UUID;

public interface PaymentMethodRepository extends BaseRepository<PaymentMethod, UUID> {

    Optional<PaymentMethod> findByLabel(String label);

}
