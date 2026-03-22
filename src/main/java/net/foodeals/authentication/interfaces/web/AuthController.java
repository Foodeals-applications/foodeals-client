package net.foodeals.authentication.interfaces.web;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.authentication.application.dtos.requests.*;
import net.foodeals.authentication.application.dtos.responses.AuthenticationResponse;
import net.foodeals.authentication.application.dtos.responses.ClientRegistredResponse;
import net.foodeals.authentication.application.dtos.responses.LoginResponse;
import net.foodeals.authentication.application.services.AuthenticationService;
import net.foodeals.authentication.application.services.FacebookService;
import net.foodeals.authentication.application.services.GoogleAuthService;
import net.foodeals.authentication.application.services.JwtService;
import net.foodeals.authentication.application.services.PasswordResetService;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping({ "api/v1/auth", "v1/auth" })
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;
    private final JwtService jwtService;
    private final FacebookService facebookService;
    private final GoogleAuthService googleAuthService;
    private final PasswordResetService passwordResetService;

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(
                service.register(request));
    }

    @PostMapping("register-client")
    public ResponseEntity<ClientRegistredResponse> register(@RequestBody @Valid RegisterClientRequest request) {

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

    @PostMapping("forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        passwordResetService.requestReset(request.email());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "If an account exists for this email, you'll receive a reset link shortly."
        ));
    }

    @PostMapping("reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Password updated successfully"
        ));
    }

    @PostMapping("/facebook")
    public ResponseEntity<LoginResponse> loginWithFacebook(@RequestBody FacebookTokenRequest request, HttpServletResponse response) {
        if (request.getAccessToken() == null || request.getAccessToken().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Missing field: accessToken");
        }
        LoginResponse loginResponse = facebookService.authenticateWithFacebook(request.getAccessToken());
        // Ajoute le token JWT dans un cookie si besoin
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        if (idToken == null || idToken.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Missing field: idToken");
        }
        return ResponseEntity.ok(googleAuthService.authenticateWithGoogle(idToken));
    }

    @PostMapping("/apple")
    public ResponseEntity<LoginResponse> authenticateWithApple(@RequestBody AppleLoginRequest request) {
        if (request.getIdentityToken() == null || request.getIdentityToken().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Missing field: identityToken");
        }
        LoginResponse response = service.authenticateWithApple(request.getIdentityToken(), request.getAuthorizationCode());
        return ResponseEntity.ok(response);
    }
}
