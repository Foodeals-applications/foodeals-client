package net.foodeals.authentication.application.services.impl;

import lombok.AllArgsConstructor;
import net.foodeals.authentication.application.dtos.requests.FacebookUser;
import net.foodeals.authentication.application.dtos.requests.LoginRequest;
import net.foodeals.authentication.application.dtos.requests.RegisterRequest;
import net.foodeals.authentication.application.dtos.responses.AuthenticationResponse;
import net.foodeals.authentication.application.dtos.responses.LoginResponse;
import net.foodeals.authentication.application.services.AuthenticationService;
import net.foodeals.authentication.application.services.FacebookService;
import net.foodeals.authentication.application.services.JwtService;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.Role;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.RoleRepository;
import net.foodeals.user.domain.repositories.UserRepository;
import net.foodeals.user.domain.valueObjects.Name;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FacebookServiceImpl implements FacebookService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final JwtService jwtService; // ton service JWT
    private final AuthenticationService authService;
    private final PasswordEncoder   passwordEncoder;
    private final AuthenticationManager authenticationManager;// ton login/register

    @Override
    public LoginResponse authenticateWithFacebook(String accessToken) {
        FacebookUser fbUser = getFacebookUser(accessToken);

        // email fallback si FB ne renvoie pas d'email
        String email = fbUser.getEmail();
        if (email == null || email.isBlank()) {
            email = fbUser.getId() + "@facebook.com";
        }

        String rawPassword = "foodeals123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    Role role = roleRepository.findByName("CLIENT").orElseThrow(() -> new RuntimeException("Role CLIENT non trouvé"));

                    // split du nom de façon robuste
                    String[] parts = fbUser.getName() != null ? fbUser.getName().trim().split("\\s+", 2) : new String[]{"", ""};
                    Name name = new Name(parts[0], parts.length > 1 ? parts[1] : "");

                    // Crée l'utilisateur — userService.create doit encoder le mot de passe
                    RegisterRequest registerRequest = new RegisterRequest(
                            name,
                            fbUser.getId() + "@facebook.com",
                            null,
                            encodedPassword,
                            true,
                            role.getId()
                    );

                    User created = userService.create(new UserRequest(
                            null, null, registerRequest.name(), null, null,
                            registerRequest.roleId(), registerRequest.email(),
                            registerRequest.isEmailVerified(), registerRequest.password(),
                            registerRequest.phone(), null, null, null, null, null, null,
                            null, null, null
                    ));

                    return userRepository.findByEmail(fbUser.getId() + "@facebook.com").orElse(created);
                });

        // NE PAS faire : LoginRequest loginRequest = new LoginRequest(user.getEmail(), user.getPassword());
        // A la place, genere les tokens directement (pas de mot de passe)
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),rawPassword ));
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);


        User authenticated = (User) authentication.getPrincipal();
        AuthenticationResponse tokens = getTokens(authenticated);

        UUID organizationId = null;
        List<String> solutions = null;
        if (user.getOrganizationEntity() != null) {
            organizationId = user.getOrganizationEntity().getId();
            solutions = user.getOrganizationEntity().getSolutions().stream()
                    .map(Solution::getName).collect(Collectors.toList());
        }

        return new LoginResponse(user.getName(), user.getEmail(), user.getPhone(),
                organizationId, solutions,
                user.getRole().getName(), user.getAvatarPath(), user.getId(),tokens);

    }

    private FacebookUser getFacebookUser(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;
        FacebookUser fbUser = restTemplate.getForObject(url, FacebookUser.class);

        if(fbUser.getEmail() == null || fbUser.getEmail().isEmpty()) {
            // Générer un email fictif à partir de l'ID Facebook
            fbUser.setEmail(fbUser.getName().split(" ")[0]+"."+fbUser.getName().split(" ")[1] + "@foodeals.com");
        }

        return fbUser;
    }

    private AuthenticationResponse getTokens(User user) {
        final Map<String, Object> extraClaims = Map.of("email", user.getEmail(), "phone", user.getPhone(), "role",
                user.getRole().getName());
        return jwtService.generateTokens(user, extraClaims);
    }



}
