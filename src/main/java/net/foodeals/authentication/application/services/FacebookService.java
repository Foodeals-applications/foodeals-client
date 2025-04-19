package net.foodeals.authentication.application.services;

import net.foodeals.authentication.application.dtos.responses.LoginResponse;

public interface FacebookService {

    LoginResponse authenticateWithFacebook(String accessToken);
}
