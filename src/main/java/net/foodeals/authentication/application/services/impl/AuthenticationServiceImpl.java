package net.foodeals.authentication.application.services.impl;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.foodeals.authentication.application.dtos.requests.LoginRequest;
import net.foodeals.authentication.application.dtos.requests.RegisterClientRequest;
import net.foodeals.authentication.application.dtos.requests.RegisterRequest;
import net.foodeals.authentication.application.dtos.responses.AppleUser;
import net.foodeals.authentication.application.dtos.responses.AuthenticationResponse;
import net.foodeals.authentication.application.dtos.responses.ClientRegistredResponse;
import net.foodeals.authentication.application.dtos.responses.LoginResponse;
import net.foodeals.authentication.application.services.AuthenticationService;
import net.foodeals.authentication.application.services.JwtService;
import net.foodeals.core.domain.entities.*;
import net.foodeals.core.repositories.CountryRepository;
import net.foodeals.core.repositories.RoleRepository;
import net.foodeals.core.repositories.UserRepository;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.application.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * AuthenticationServiceImpl
 */
@Service
@RequiredArgsConstructor
@Slf4j
class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CountryRepository countryRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationResponse register(RegisterRequest request) {
        final User user = userService.create

                (new UserRequest(null, null, request.name(), null, null, request.roleId(), request.email(),
                        request.isEmailVerified(), request.password(), request.phone(), null, null, null, null, null, null,
                        null, null, null));
        return handleRegistration(user);
    }

    @Override
    public ClientRegistredResponse registerClient(RegisterClientRequest request) {
        Role role =roleRepository.findByName("CLIENT").get();
        Country country=countryRepository.findByName(request.countryName());
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);
        user.setDateOfBirth(request.birthDate());
        user =userRepository.save(user);
        AuthenticationResponse token = getTokens(user);
        return new ClientRegistredResponse(user.getId(),user.getName(), user.getEmail(), user.getPhone(),
                null, null,
                user.getRole().getName(), user.getAvatarPath(), token);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(authentication);

            final User user = userService.findByEmail(request.email());

            // Generate token (assuming getTokens returns a token string)
            AuthenticationResponse token = getTokens(user);

            // Create and return the LoginResponse
            UUID organizationId = null;
            List<String> solutions = null;
            if (user.getOrganizationEntity() != null) {
                organizationId = user.getOrganizationEntity().getId();
                solutions = user.getOrganizationEntity().getSolutions().stream()
                        .map(Solution::getName).collect(Collectors.toList());
            }

            return new LoginResponse(user.getName(), user.getEmail(), user.getPhone(),
                    organizationId, solutions,
                    user.getRole().getName(), user.getAvatarPath(), user.getId(), token);
        } catch (Exception e) {
            // Handle exception
            throw new RuntimeException("Login failed", e);
        }
    }


    @Transactional
    public boolean verifyToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtService.isTokenValid(token, userDetails);
        } catch (Exception e) {
            log.error("Token verification failed: {}", e.getMessage());
            return false;
        }
    }



    public LoginResponse authenticateWithApple(String identityToken, String authorizationCode) {
        AppleUser appleUser = verifyAppleToken(identityToken);

        Optional<User> userOpt = userRepository.findByEmail(appleUser.getEmail());
        User user;

        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            Role role = roleRepository.findByName("CLIENT").orElseThrow();
            user = new User();
            user.setName(new Name(appleUser.getFirstName(),appleUser.getLastName()));
            user.setEmail(appleUser.getEmail());
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // mot de passe aléatoire
            user.setRole(role);
            userRepository.save(user);
        }

        // Générer JWT + refreshToken

        LoginRequest loginRequest = new LoginRequest(user.getEmail(), user.getPassword());
        return this.login(loginRequest);

    }

    private AppleUser verifyAppleToken(String identityToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(identityToken);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            String email = claims.getStringClaim("email");
            String firstName = claims.getStringClaim("given_name");
            String lastName = claims.getStringClaim("family_name");

            return new AppleUser(firstName, lastName, email);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Apple identity token", e);
        }
    }

    private AuthenticationResponse handleRegistration(User user) {
        return getTokens(user);
    }

    private AuthenticationResponse getTokens(User user) {
        final Map<String, Object> extraClaims = Map.of("email", user.getEmail(), "phone", user.getPhone(), "role",
                user.getRole().getName());
        return jwtService.generateTokens(user, extraClaims);
    }
}