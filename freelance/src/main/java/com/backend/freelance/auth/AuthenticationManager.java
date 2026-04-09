package com.backend.freelance.auth;

import com.backend.freelance.exception.ExceptionHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationManager {

    public static UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            ExceptionHelper.UNAUTHORIZED.throwCustomException("User is not authenticated", null);
        }

        if (!authentication.isAuthenticated()) {
            ExceptionHelper.UNAUTHORIZED.throwCustomException("User is not authenticated", null);
        }
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            ExceptionHelper.UNAUTHORIZED.throwCustomException("User is not authenticated", null);
        }
        return (UserDetails) authentication.getPrincipal();
    }
}
