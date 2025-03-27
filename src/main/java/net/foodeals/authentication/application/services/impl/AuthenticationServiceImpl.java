package net.foodeals.authentication.application.services.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.foodeals.authentication.application.dtos.requests.LoginRequest;
import net.foodeals.authentication.application.dtos.requests.RegisterRequest;
import net.foodeals.authentication.application.dtos.responses.AuthenticationResponse;
import net.foodeals.authentication.application.dtos.responses.LoginResponse;
import net.foodeals.authentication.application.services.AuthenticationService;
import net.foodeals.authentication.application.services.JwtService;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

/**
 * AuthenticationServiceImpl
 */
@Service
@RequiredArgsConstructor
@Slf4j
class AuthenticationServiceImpl implements AuthenticationService {
	private final UserService userService;
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest request) {
		final User user = userService.create

		(new UserRequest(null, null, request.name(), null, null, request.roleId(), request.email(),
				request.isEmailVerified(),request.password(), request.phone(), null, null, null, null, null, null,
				null, null, null));
		return handleRegistration(user);
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
			UUID organizationId=null;
			List<String>solutions=null;
			if(user.getOrganizationEntity()!=null) {
				organizationId=user.getOrganizationEntity().getId();
				solutions=user.getOrganizationEntity().getSolutions().stream()
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

	private AuthenticationResponse handleRegistration(User user) {
		return getTokens(user);
	}

	private AuthenticationResponse getTokens(User user) {
		final Map<String, Object> extraClaims = Map.of("email", user.getEmail(), "phone", user.getPhone(), "role",
				user.getRole().getName());
		return jwtService.generateTokens(user, extraClaims);
	}
}