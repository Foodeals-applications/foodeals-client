package net.foodeals.product.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.product.domain.entities.PaymentMethodProduct;

public interface PaymentMethodProductRepository extends BaseRepository<PaymentMethodProduct, UUID> {

    Optional<PaymentMethodProduct> findByMethodName(String methodName);
}
