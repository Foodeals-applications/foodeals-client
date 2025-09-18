package net.foodeals.order.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CouponResponse
{
        String id;
        String name ;
        String code;
        Float discount;
        Date startsAt;
        Date endsAt;
        Boolean isEnabled ;
}