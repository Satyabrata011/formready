package com.satyabrata.formready.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class RegistrationService {

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(AppUserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegistrationForm form) {
        String normalizedEmail = form.getEmail().trim().toLowerCase(Locale.ROOT);

        if (users.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyRegisteredException();
        }

        AppUser user = new AppUser(
                form.getFullName().trim(),
                normalizedEmail,
                passwordEncoder.encode(form.getPassword())
        );
        users.save(user);
    }

    public static class EmailAlreadyRegisteredException extends RuntimeException {
    }
}
