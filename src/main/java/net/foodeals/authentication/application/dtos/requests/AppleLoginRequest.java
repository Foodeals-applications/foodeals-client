package net.foodeals.authentication.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppleLoginRequest {
    private String identityToken;
    private String authorizationCode;
}
