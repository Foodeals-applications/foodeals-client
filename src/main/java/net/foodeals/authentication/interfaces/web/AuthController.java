package net.foodeals.authentication.interfaces.web;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.authentication.application.dtos.requests.LoginRequest;
import net.foodeals.authentication.application.dtos.requests.RegisterRequest;
import net.foodeals.authentication.application.dtos.requests.VerifyTokenRequest;
import net.foodeals.authentication.application.dtos.responses.AuthenticationResponse;
import net.foodeals.authentication.application.dtos.responses.LoginResponse;
import net.foodeals.authentication.application.services.AuthenticationService;
import net.foodeals.authentication.application.services.JwtService;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;
    private final JwtService jwtService;

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(
                service.register(request));
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
}
