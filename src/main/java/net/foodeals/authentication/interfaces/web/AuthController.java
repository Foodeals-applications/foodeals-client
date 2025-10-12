package net.foodeals.authentication.interfaces.web;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.authentication.application.dtos.requests.*;
import net.foodeals.authentication.application.dtos.responses.AuthenticationResponse;
import net.foodeals.authentication.application.dtos.responses.LoginResponse;
import net.foodeals.authentication.application.services.AuthenticationService;
import net.foodeals.authentication.application.services.FacebookService;
import net.foodeals.authentication.application.services.GoogleAuthService;
import net.foodeals.authentication.application.services.JwtService;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;
    private final JwtService jwtService;
    private final FacebookService facebookService;
    private final GoogleAuthService googleAuthService;

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(
                service.register(request));
    }

    @PostMapping("register-client")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterClientRequest request) {

        return ResponseEntity.ok(
                service.registerClient(request));
    }

    @PostMapping("login")
    @Transactional
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = service.login(request);
        addTokenCookie(response, loginResponse.getToken().accessToken());
        return ResponseEntity.ok(loginResponse);
    }
    
    
    private void addTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("authjs.session-token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge((int) jwtService.getAccessTokenExpirationSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    @PostMapping("verify-token")
    @Transactional
    public ResponseEntity<Boolean> verifyToken(@RequestBody @Valid VerifyTokenRequest request) {
        boolean isValid = service.verifyToken(request.token());
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/facebook")
    public ResponseEntity<LoginResponse> loginWithFacebook(@RequestBody FacebookTokenRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = facebookService.authenticateWithFacebook(request.getAccessToken());
        // Ajoute le token JWT dans un cookie si besoin
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        return ResponseEntity.ok(googleAuthService.authenticateWithGoogle(idToken));
    }

    @PostMapping("/apple")
    public ResponseEntity<LoginResponse> authenticateWithApple(@RequestBody AppleLoginRequest request) {
        LoginResponse response = service.authenticateWithApple(request.getIdentityToken(), request.getAuthorizationCode());
        return ResponseEntity.ok(response);
    }
}
