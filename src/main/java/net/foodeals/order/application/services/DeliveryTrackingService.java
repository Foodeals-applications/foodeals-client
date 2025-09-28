package net.foodeals.order.application.services;

import lombok.RequiredArgsConstructor;
import net.foodeals.delivery.domain.entities.Delivery;
import net.foodeals.delivery.domain.repositories.DeliveryRepository;
import net.foodeals.order.application.dtos.responses.DeliveryTrackingResponse;
import net.foodeals.order.application.dtos.responses.DeliveryTrackingResponse;
import net.foodeals.order.application.dtos.responses.LocationDto;
import net.foodeals.order.application.dtos.responses.PositionDto;
import net.foodeals.order.domain.entities.Order;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryTrackingService {

    private final DeliveryRepository deliveryRepository;
    private final OrderService orderService;

    public DeliveryTrackingResponse getTracking(UUID orderId) {
        Order order =orderService.findById(orderId);
        Delivery delivery = order.getDelivery();

        // 🚚 Récupération des infos du livreur
       Integer deliveryPersonId = delivery.getDeliveryBoy().getId();
        String deliveryPersonName = delivery.getDeliveryBoy().getName().firstName()+" "+delivery.getDeliveryBoy().getName().lastName();
        String deliveryPersonPhone = delivery.getDeliveryBoy().getPhone();

        // 🚩 Position courante (DeliveryPosition -> Coordinates)
        LocationDto currentLocation = LocationDto.builder()
                .latitude(delivery.getDeliveryPosition().getCoordinates().latitude())
                .longitude(delivery.getDeliveryPosition().getCoordinates().longitude())
                .build();

        // 🏠 Position du client (Address -> Coordinates)
        LocationDto customerLocation = LocationDto.builder()
                .latitude(delivery.getOrder().getShippingAddress().getCoordinates().latitude())
                .longitude(delivery.getOrder().getShippingAddress().getCoordinates().longitude())
                .build();

        // ETA -> basé sur duration (en minutes, par ex.)
        Instant estimatedArrival = Instant.now().plusSeconds(delivery.getDuration() * 60);

        // 🚦 Conversion status Delivery -> status DeliveryTracking
        String status = switch (delivery.getStatus()) {
            case ASSIGNED -> "assigned";
            case PICKED_UP -> "picked_up";
            case IN_PROGRESS -> "on_way";
            case NEARBY -> "nearby";
            case DELIVERED -> "delivered";
            default -> "assigned";
        };

        // TODO: Ici tu peux plugger ton moteur de routage (Google Maps API, OSRM, etc.)
        List<PositionDto> route = List.of(
                PositionDto.builder()
                        .latitude(currentLocation.getLatitude())
                        .longitude(currentLocation.getLongitude())
                        .build(),
                PositionDto.builder()
                        .latitude(customerLocation.getLatitude())
                        .longitude(customerLocation.getLongitude())
                        .build()
        );

        return DeliveryTrackingResponse.builder()
                .orderId(delivery.getOrder().getId())
                .deliveryPersonId(deliveryPersonId)
                .deliveryPersonName(deliveryPersonName)
                .deliveryPersonPhone(deliveryPersonPhone)
                .vehicle("motorcycle") // 👉 tu peux mapper ça depuis une propriété de User/Delivery
                .currentLocation(currentLocation)
                .customerLocation(customerLocation)
                .estimatedArrival(estimatedArrival)
                .status(status)
                .route(route)
                .build();
    }}