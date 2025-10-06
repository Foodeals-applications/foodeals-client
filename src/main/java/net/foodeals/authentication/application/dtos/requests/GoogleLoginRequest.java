package net.foodeals.authentication.application.dtos.requests;

import lombok.Data;

@Data
public class GoogleLoginRequest {
    private String idToken;
}