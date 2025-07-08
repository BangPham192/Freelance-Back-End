package com.backend.freelance.controllers;

import com.backend.freelance.auth.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
public class BaseController {

    public static UserDetails getCurrentUser() {
        return AuthenticationManager.getCurrentUser();
    }
}
