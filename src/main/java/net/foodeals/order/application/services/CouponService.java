package net.foodeals.order.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.Coupon;
import net.foodeals.core.domain.entities.User;
import net.foodeals.order.application.dtos.requests.CouponRequest;
import net.foodeals.order.application.dtos.responses.ActivateCouponResponse;
import net.foodeals.order.application.dtos.responses.AddCouponResponse;
import net.foodeals.order.application.dtos.responses.CouponsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CouponService extends CrudService<Coupon, UUID, CouponRequest> {

    Coupon toggleIsEnabled(UUID id);

    Page findAllEnabled(Pageable pageable);

    Page findAllExpired(Pageable pageable);

    CouponsResponse getUserCoupons(User user);

    AddCouponResponse addCoupon(User user, String code);

    ActivateCouponResponse activateCoupon(UUID couponId);
}
