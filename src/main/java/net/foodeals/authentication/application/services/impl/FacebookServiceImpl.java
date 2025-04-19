package net.foodeals.authentication.application.services.impl;

import lombok.AllArgsConstructor;
import net.foodeals.authentication.application.dtos.requests.FacebookUser;
import net.foodeals.authentication.application.dtos.requests.LoginRequest;
import net.foodeals.authentication.application.dtos.requests.RegisterRequest;
import net.foodeals.authentication.application.dtos.responses.LoginResponse;
import net.foodeals.authentication.application.services.AuthenticationService;
import net.foodeals.authentication.application.services.FacebookService;
import net.foodeals.authentication.application.services.JwtService;
import net.foodeals.user.domain.entities.Role;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.RoleRepository;
import net.foodeals.user.domain.repositories.UserRepository;
import net.foodeals.user.domain.valueObjects.Name;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FacebookServiceImpl implements FacebookService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService; // ton service JWT
    private final AuthenticationService authService; // ton login/register

    @Override
    public LoginResponse authenticateWithFacebook(String accessToken) {
        FacebookUser fbUser = getFacebookUser(accessToken);

        Optional<User> userOpt = userRepository.findByEmail(fbUser.getEmail());

        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            Role role=roleRepository.findByName("CLIENT").orElse(null);
            // Création d’un nouvel utilisateur
            RegisterRequest registerRequest = new RegisterRequest(new Name(fbUser.getName().split(" ")[0],fbUser.getName().split(" ")[1]),
                    fbUser.getEmail(), null,UUID.randomUUID().toString(),true,role.getId());
            authService.register(registerRequest);
            user = userRepository.findByEmail(fbUser.getEmail()).orElseThrow();
        }

        // Login → génère token
        LoginRequest loginRequest = new LoginRequest(user.getEmail(),user.getPassword());

        return authService.login(loginRequest);
    }

    private FacebookUser getFacebookUser(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;
        return restTemplate.getForObject(url, FacebookUser.class);
    }



}
