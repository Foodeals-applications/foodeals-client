package net.foodeals.authentication.application.services;

import net.foodeals.authentication.application.dtos.requests.GoogleLoginRequest;
import net.foodeals.authentication.application.dtos.responses.LoginResponse;

public interface GoogleAuthService {

    public LoginResponse authenticateWithGoogle(String idToken);
}
