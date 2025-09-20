package net.foodeals.notification.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationCountResponse {
    private long donations;
    private long favorites;
    private long coupons;
}