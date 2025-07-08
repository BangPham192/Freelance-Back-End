package com.backend.freelance.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationManager {

    public static UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("No authentication found");
        }

        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("Authentication principal is not an instance of UserDetails");
        }
        return (UserDetails) authentication.getPrincipal();
    }
}
