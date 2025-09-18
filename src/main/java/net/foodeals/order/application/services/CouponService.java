package net.foodeals.order.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.order.application.dtos.requests.CouponRequest;
import net.foodeals.order.domain.entities.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.UUID;

public interface CouponService extends CrudService<Coupon, UUID, CouponRequest> {

    Coupon toggleIsEnabled(UUID id);

   Page findAllEnabled(Pageable pageable) ;

     Page findAllExpired(Pageable pageable) ;
}
