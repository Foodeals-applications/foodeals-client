package net.foodeals.authentication.application.services.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import lombok.RequiredArgsConstructor;
import net.foodeals.authentication.application.dtos.responses.AuthenticationResponse;
import net.foodeals.authentication.application.dtos.responses.LoginResponse;
import net.foodeals.authentication.application.services.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.gson.GsonFactory;
import net.foodeals.authentication.application.services.JwtService;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.user.domain.entities.Role;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.RoleRepository;
import net.foodeals.user.domain.repositories.UserRepository;
import net.foodeals.user.domain.valueObjects.Name;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public LoginResponse authenticateWithGoogle(String idToken) {
        GoogleIdToken.Payload payload = verifyGoogleToken(idToken);
        if (payload == null) {
            throw new RuntimeException("Invalid Google ID Token");
        }

        String email = payload.getEmail();
        if (email == null || email.isBlank()) {
            // Fallback if Google doesn't provide email for some reason.
            email = payload.getSubject() + "@foodeals.com";
        }
        final String userEmail = email;

        String fullName = (String) payload.get("name");
        String givenName = (String) payload.get("given_name");
        String familyName = (String) payload.get("family_name");

        Name name = buildName(fullName, givenName, familyName);

        User user = userRepository.findByEmail(userEmail).orElseGet(() -> {
            Role role = roleRepository.findByName("CLIENT")
                    .orElseThrow(() -> new RuntimeException("Role CLIENT not found"));

            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(userEmail);
            newUser.setIsEmailVerified(true);
            newUser.setRole(role);
            newUser.setSocialLogin(true);
            newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            return userRepository.save(newUser);
        });

        // Ensure existing accounts created by older versions still have the required fields.
        if (user.getRole() == null) {
            Role role = roleRepository.findByName("CLIENT")
                    .orElseThrow(() -> new RuntimeException("Role CLIENT not found"));
            user.setRole(role);
        }
        if (user.getName() == null) {
            user.setName(name);
        }
        if (user.getIsEmailVerified() == null || !user.getIsEmailVerified()) {
            user.setIsEmailVerified(true);
        }
        if (user.getSocialLogin() == null || !user.getSocialLogin()) {
            user.setSocialLogin(true);
        }
        user = userRepository.save(user);

        AuthenticationResponse tokens = getTokens(user);

        UUID organizationId = null;
        List<String> solutions = null;
        if (user.getOrganizationEntity() != null) {
            organizationId = user.getOrganizationEntity().getId();
            if (user.getOrganizationEntity().getSolutions() != null) {
                solutions = user.getOrganizationEntity().getSolutions().stream()
                        .map(Solution::getName)
                        .collect(Collectors.toList());
            }
        }

        return new LoginResponse(
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                organizationId,
                solutions,
                user.getRole().getName(),
                user.getAvatarPath(),
                user.getId(),
                tokens
        );
    }

    private Name buildName(String fullName, String givenName, String familyName) {
        String first = givenName;
        String last = familyName;

        if ((first == null || first.isBlank()) && fullName != null && !fullName.isBlank()) {
            String[] parts = fullName.trim().split("\\s+", 2);
            first = parts[0];
            last = parts.length > 1 ? parts[1] : "";
        }

        if (first == null) first = "";
        if (last == null) last = "";

        return new Name(first, last);
    }


    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString) {
        try {
            if (idTokenString == null || idTokenString.isBlank()) {
                throw new IllegalArgumentException("Google ID Token is null or empty");
            }

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(List.of(
                            "612106481875-hodimi65ojh6qk6shs754qdeihq0kt4t.apps.googleusercontent.com",
                            "612106481875-r4jvebfvisteujqknei5hefphmn5c6uf.apps.googleusercontent.com",
                            "612106481875-ov63in07kf6rnr36i8ihsod2kti7nvce.apps.googleusercontent.com"
                    ))
                    .setIssuer("https://accounts.google.com")
                    .build();

            debugGoogleToken(idTokenString);

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("Invalid ID token: verification failed");
            }

            return idToken.getPayload();

        } catch (Exception e) {
            System.out.println("❌ Erreur de vérification Google ID Token: {}"+e.getMessage());
            throw new RuntimeException("Failed to verify Google ID token: " + e.getMessage(), e);
        }
    }


    public void debugGoogleToken(String idTokenString) {
        try {
            String[] parts = idTokenString.split("\\.");
            if (parts.length < 2) {
                System.out.println("Token invalide : il ne contient pas 3 parties.");
                return;
            }

            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

            System.out.println("HEADER:\n" + headerJson);
            System.out.println("PAYLOAD:\n" + payloadJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AuthenticationResponse getTokens(User user) {
        // Map.of(...) does not allow null values; Google users often don't have a phone yet.
        Map<String, Object> extraClaims = new HashMap<>();

        if (user.getEmail() != null) {
            extraClaims.put("email", user.getEmail());
        }
        if (user.getPhone() != null) {
            extraClaims.put("phone", user.getPhone());
        }
        if (user.getRole() != null && user.getRole().getName() != null) {
            extraClaims.put("role", user.getRole().getName());
        }

        return jwtService.generateTokens(user, extraClaims);
    }
}
