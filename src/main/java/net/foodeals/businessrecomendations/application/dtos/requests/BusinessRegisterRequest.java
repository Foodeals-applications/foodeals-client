package net.foodeals.businessrecomendations.application.dtos.requests;

import lombok.Data;

@Data
public class BusinessRegisterRequest {
    private String businessName;
    private String ownerName;
    private String email;
    private String phone;
    private String address;
    private String category;
}