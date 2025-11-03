package net.foodeals.authentication.application.services.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import lombok.RequiredArgsConstructor;
import net.foodeals.authentication.application.dtos.requests.GoogleLoginRequest;
import net.foodeals.authentication.application.dtos.requests.LoginRequest;
import net.foodeals.authentication.application.dtos.requests.RegisterRequest;
import net.foodeals.authentication.application.dtos.responses.AuthenticationResponse;
import net.foodeals.authentication.application.dtos.responses.LoginResponse;
import net.foodeals.authentication.application.services.AuthenticationService;
import net.foodeals.authentication.application.services.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import net.foodeals.authentication.application.services.JwtService;
import net.foodeals.core.domain.entities.Name;
import net.foodeals.core.domain.entities.Role;
import net.foodeals.core.domain.entities.User;
import net.foodeals.core.repositories.RoleRepository;
import net.foodeals.core.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationService authService;


    @Override
    public LoginResponse authenticateWithGoogle(String idToken) {
        GoogleIdToken.Payload payload = verifyGoogleToken(idToken);
        if (payload == null) {
            throw new RuntimeException("Invalid Google ID Token");
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String givenName = (String) payload.get("given_name");
        String familyName = (String) payload.get("family_name");

        Optional<User> userOpt = userRepository.findByEmail(email);
        User user;

        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            Role role = roleRepository.findByName("CLIENT")
                    .orElseThrow(() -> new RuntimeException("Role CLIENT not found"));

            // Créer un nouvel utilisateur à partir du payload Google
            RegisterRequest registerRequest = new RegisterRequest(
                    new Name(givenName != null ? givenName : name.split(" ")[0],
                            familyName != null ? familyName : name.split(" ")[1]),
                    email,
                    null, // pas de mot de passe (auth externe)
                    UUID.randomUUID().toString(),
                    true,
                    role.getId()
            );

            authService.register(registerRequest);
            user = userRepository.findByEmail(email).orElseThrow();
        }

        // ⚡ Connexion de l’utilisateur
        LoginRequest loginRequest = new LoginRequest(user.getEmail(), user.getPassword());
        return authService.login(loginRequest);
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
        final Map<String, Object> extraClaims = Map.of("email", user.getEmail(), "phone", user.getPhone(), "role",
                user.getRole().getName());
        return jwtService.generateTokens(user, extraClaims);
    }
}
