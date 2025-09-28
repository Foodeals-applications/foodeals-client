package net.foodeals.order.application.dtos.responses;


import lombok.Builder;
import lombok.Data;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class DeliveryTrackingResponse {
    private UUID orderId;
    private Integer deliveryPersonId;
    private String deliveryPersonName;
    private String deliveryPersonPhone;
    private String vehicle; // car | motorcycle | bicycle | foot
    private LocationDto currentLocation;
    private LocationDto customerLocation;
    private Instant estimatedArrival;
    private String status; // assigned | picked_up | on_way | nearby | delivered
    private List<PositionDto> route;
}
