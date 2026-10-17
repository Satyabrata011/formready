package com.satyabrata.formready.profile;

import com.satyabrata.formready.user.AppUser;
import com.satyabrata.formready.user.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Locale;

@Service
public class ProfileService {

    private final AppUserRepository users;
    private final ProfileRepository profiles;

    public ProfileService(AppUserRepository users, ProfileRepository profiles) {
        this.users = users;
        this.profiles = profiles;
    }

    @Transactional(readOnly = true)
    public ProfileForm getProfileForm(String email) {
        AppUser user = findUser(email);
        return profiles.findByUserId(user.getId())
                .map(ProfileForm::fromProfile)
                .orElseGet(() -> newProfileForm(user));
    }

    @Transactional(readOnly = true)
    public boolean hasProfile(String email) {
        return profiles.existsByUserId(findUser(email).getId());
    }

    @Transactional
    public void saveProfile(String email, ProfileForm form) {
        AppUser user = findUser(email);
        Profile profile = profiles.findByUserId(user.getId()).orElseGet(() -> new Profile(user.getId()));
        profile.update(form);
        profiles.save(profile);
    }

    public boolean isOptionalWebUrl(String value) {
        if (value == null || value.isBlank()) {
            return true;
        }

        try {
            URI uri = URI.create(value.trim());
            String scheme = uri.getScheme();
            return uri.getHost() != null && scheme != null
                    && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme));
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private AppUser findUser(String email) {
        return users.findByEmail(email.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalStateException("Signed-in account was not found"));
    }

    private ProfileForm newProfileForm(AppUser user) {
        String[] nameParts = user.getFullName().trim().split("\\s+", 2);
        ProfileForm form = new ProfileForm();
        form.setFirstName(nameParts[0]);
        form.setLastName(nameParts.length > 1 ? nameParts[1] : "-");
        return form;
    }
}
