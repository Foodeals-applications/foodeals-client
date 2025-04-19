package net.foodeals.authentication.application.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FacebookTokenRequest {

    private String accessToken;
}
