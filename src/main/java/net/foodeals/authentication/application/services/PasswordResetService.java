package net.foodeals.authentication.application.services;

public interface PasswordResetService {
    void requestReset(String email);
    void resetPassword(String token, String newPassword);
}
