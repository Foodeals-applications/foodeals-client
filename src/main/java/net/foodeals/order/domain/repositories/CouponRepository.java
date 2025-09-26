package net.foodeals.order.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.order.domain.entities.Coupon;
import net.foodeals.user.domain.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponRepository extends BaseRepository<Coupon, UUID> {

    Page findAllByIsEnabledTrue(Pageable pageable);

    Page findAllByEndsAtBefore(Date now, Pageable pageable);

    List<Coupon> findByUser(User user );

    Optional<Coupon> findByCodeAndUser(String code, User user);
}
