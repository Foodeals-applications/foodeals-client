package net.foodeals.order.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponsResponse {
    private List<CouponResponse> active;
    private List<CouponResponse> expired;
}