package net.foodeals.order.application.dtos.responses;

import lombok.*;
import net.foodeals.order.domain.entities.Coupon;

import java.util.Date;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse {
    private UUID id;
    private String code;
    private String name;
    private Float discount;
    private Date endsAt;
    private Boolean isEnabled;
    private UUID subEntityId;

    public static CouponResponse fromEntity(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode(),
                coupon.getName(),
                coupon.getDiscount(),
                coupon.getEndsAt(),
                coupon.getIsEnabled(),
                coupon.getSubEntity() != null ? coupon.getSubEntity().getId() : null
        );
    }
}