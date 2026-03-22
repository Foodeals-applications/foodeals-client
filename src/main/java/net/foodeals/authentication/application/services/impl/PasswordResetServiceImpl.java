package net.foodeals.authentication.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.foodeals.authentication.application.services.PasswordResetService;
import net.foodeals.authentication.domain.entities.PasswordResetToken;
import net.foodeals.authentication.domain.repositories.PasswordResetTokenRepository;
import net.foodeals.common.services.EmailService;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final long TOKEN_EXPIRATION_MINUTES = 30;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public void requestReset(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // Don't leak account existence.
            return;
        }

        User user = userOpt.get();

        // Invalidate previous tokens for this user
        tokenRepository.markAllUsedForUser(user.getId(), Instant.now());

        String token = UUID.randomUUID().toString().replace("-", "");
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiresAt(Instant.now().plus(TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES));
        tokenRepository.save(resetToken);

        String subject = "Reset your Foodeals password";
        String body = """
                We received a request to reset your Foodeals password.

                Use this code to reset your password:
                %s

                This code expires in %d minutes.
                If you didn't request this, you can ignore this email.
                """.formatted(token, TOKEN_EXPIRATION_MINUTES);

        try {
            emailService.sendEmail(user.getEmail(), subject, body);
        } catch (Exception e) {
            log.error("SMTP error while sending password reset email", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "SMTP error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Invalid or expired token"));

        if (resetToken.isUsed() || resetToken.isExpired()) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid or expired token");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.markUsed();
        tokenRepository.save(resetToken);
    }
}
