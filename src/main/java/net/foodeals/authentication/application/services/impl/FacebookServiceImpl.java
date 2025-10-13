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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
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
        // 1️⃣ Récupérer les infos Facebook via l’API Graph
        FacebookUser fbUser = getFacebookUser(accessToken);

        if (fbUser == null || fbUser.getId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Facebook token invalide ou expiré");
        }

        // 2️⃣ Gestion de l’email fallback
        String email = (fbUser.getEmail() != null && !fbUser.getEmail().isBlank())
                ? fbUser.getEmail()
                : fbUser.getId() + "@foodeals.com";

        // 3️⃣ Vérifier si l’utilisateur existe déjà
        User user = userRepository.findByFacebookId(fbUser.getId())
                .or(() -> userRepository.findByEmail(email))
                .orElseGet(() -> {
                    Role role = roleRepository.findByName("CLIENT")
                            .orElseThrow(() -> new RuntimeException("Role CLIENT non trouvé"));

                    // Split du nom
                    String[] parts = fbUser.getName() != null
                            ? fbUser.getName().trim().split("\\s+", 2)
                            : new String[]{"", ""};
                    Name name = new Name(parts[0], parts.length > 1 ? parts[1] : "");

                    // Créer nouvel utilisateur social
                    User newUser = new User();
                    newUser.setName(name);
                    newUser.setEmail(email);
                    newUser.setFacebookId(fbUser.getId());
                    newUser.setIsEmailVerified(true);
                    newUser.setRole(role);

                    newUser.setSocialLogin(true); // ⚠️ nouveau champ booléen pour social login


                    return userRepository.save(newUser);
                });

        // 4️⃣ Générer les tokens JWT directement (pas besoin d’authManager)
        AuthenticationResponse tokens = getTokens(user);

        // 5️⃣ Récupérer organisation et solutions si présentes
        UUID organizationId = null;
        List<String> solutions = null;

        if (user.getOrganizationEntity() != null) {
            organizationId = user.getOrganizationEntity().getId();
            solutions = user.getOrganizationEntity().getSolutions()
                    .stream()
                    .map(Solution::getName)
                    .collect(Collectors.toList());
        }

        // 6️⃣ Construire la réponse finale
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
        Map<String, Object> extraClaims = new HashMap<>();

        if (user.getEmail() != null)
            extraClaims.put("email", user.getEmail());
        if (user.getPhone() != null)
            extraClaims.put("phone", user.getPhone());
        if (user.getRole() != null && user.getRole().getName() != null)
            extraClaims.put("role", user.getRole().getName());

        return jwtService.generateTokens(user, extraClaims);
    }




}
