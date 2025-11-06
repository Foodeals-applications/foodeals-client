package net.foodeals.order.application.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDetailsResponse {

    private String estimatedDeliveryTime;
    private Date estimatedDeliveryDate;
    private String deliveryDateFormatted;

    private DeliveryPersonResponse deliveryPerson;
    private DeliveryAddressResponse deliveryAddress;
    private List<ItemResponse> items;
    private PaymentResponse payment;

    private String status;
    private List<StatusHistoryResponse> statusHistory;
    private TotalsResponse totals;

    // ======= Subclasses =======

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class DeliveryPersonResponse {
        private String id;
        private String name;
        private String distance;
        private String image;
        private String phone;
        private double rating;
        private String vehicleType;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class DeliveryAddressResponse {
        private String fullAddress;
        private String street;
        private String streetNumber;
        private String city;
        private String postalCode;
        private String country;
        private Coordinates coordinates;
        private String additionalInfo;

        @AllArgsConstructor
        @NoArgsConstructor
        @Setter
        @Getter
        public static class Coordinates {
            private double latitude;
            private double longitude;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class ItemResponse {
        private String id;
        private String productId;
        private String name;
        private int quantity;
        private double price;
        private double originalPrice;
        private String image;
        private String thumbnail;
        private String notes;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class PaymentResponse {
        private String method;
        private String status;
        private String cardLastFourDigits;
        private String cardBrand;
        private String transactionId;
        private double amount;
        private String currency;
        private Date paidAt;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class StatusHistoryResponse {
        private String status;
        private Date timestamp;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class TotalsResponse {
        private double subtotal;
        private double deliveryFee;
        private double tax;
        private double discount;
        private double total;
    }
}
