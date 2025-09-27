package net.foodeals.order.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.order.application.dtos.requests.CouponRequest;
import net.foodeals.order.application.dtos.responses.ActivateCouponResponse;
import net.foodeals.order.application.dtos.responses.AddCouponResponse;
import net.foodeals.order.application.dtos.responses.CouponResponse;
import net.foodeals.order.application.dtos.responses.CouponsResponse;
import net.foodeals.order.application.services.CouponService;
import net.foodeals.order.domain.entities.Coupon;
import net.foodeals.order.domain.exceptions.CouponNotFoundException;
import net.foodeals.order.domain.repositories.CouponRepository;
import net.foodeals.user.domain.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<Coupon> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Coupon> findAll(Integer pageNumber, Integer pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public Coupon findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));
    }

    @Override
    public Coupon create(CouponRequest request) {
        final Coupon coupon = mapper.map(request, Coupon.class);
        // todo: add subentity
        return repository.save(coupon);
    }

    @Override
    public Coupon update(UUID id, CouponRequest request) {
        final Coupon coupon = findById(id);
        mapper.map(request, coupon);
        return repository.save(coupon);
    }


    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id))
            throw new CouponNotFoundException(id);

        repository.softDelete(id);
    }

    public Coupon toggleIsEnabled(UUID id) {
        final Coupon coupon = findById(id);
        coupon.toggleIsEnabled();
        return repository.save(coupon);
    }


    public Page findAllEnabled(Pageable pageable) {
        return repository.findAllByIsEnabledTrue(pageable);
    }

    public Page findAllExpired(Pageable pageable) {
        return repository.findAllByEndsAtBefore(new Date(),pageable);
    }


    @Override
    public CouponsResponse getUserCoupons(User user) {
        List<Coupon> coupons = repository.findByUser(user);
        LocalDate today = LocalDate.now();

        List<CouponResponse> active = coupons.stream()
                .filter(c -> Boolean.TRUE.equals(c.getIsEnabled()) && c.getEndsAt().after(new Date()))
                .map(CouponResponse::fromEntity)
                .toList();

        List<CouponResponse> expired = coupons.stream()
                .filter(c -> c.getEndsAt().before(new Date()))
                .map(CouponResponse::fromEntity)
                .toList();


        return new CouponsResponse(active, expired);
    }

    @Override
    public AddCouponResponse addCoupon(User user, String code) {
        // Vérifier si le coupon existe en DB
        Optional<Coupon> couponOpt = repository.findByCode(code);
        if (couponOpt.isEmpty()) {
            throw new RuntimeException("Invalid coupon code");
        }

        Coupon coupon = couponOpt.get();

        // Vérifier si le coupon est encore valide
        if (!Boolean.TRUE.equals(coupon.getIsEnabled()) || coupon.getEndsAt().before(new Date())) {
            throw new RuntimeException("Coupon expired or disabled");
        }

        // Vérifier si l'utilisateur l'a déjà utilisé
        if (repository.findByCodeAndUser(code, user).isPresent()) {
            return new AddCouponResponse(CouponResponse.fromEntity(coupon), false);
        }

        // Associer le coupon à l'utilisateur
        coupon.setUser(user);
        repository.save(coupon);

        return new AddCouponResponse(CouponResponse.fromEntity(coupon), true);
    }


    @Override
    public ActivateCouponResponse activateCoupon(UUID couponId) {
        Optional<Coupon> couponOpt =repository.findById(couponId);
        if (couponOpt.isEmpty()) return new ActivateCouponResponse(false);

        Coupon coupon = couponOpt.get();
        coupon.toggleIsEnabled();
        repository.save(coupon);

        return new ActivateCouponResponse(true);
    }
}
