package net.foodeals.organizationEntity.application.dtos.responses;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatisticsResponse {
    private BigDecimal totalWithDelivery;
    private BigDecimal totalWithoutDelivery;
    private long deliveredCount;
    private long notDeliveredCount;
    private long dineInCount;
    private long takeAwayCount;
    private long deliveryCount;
    private long canceledCount;
    private long archivedCount;
}
