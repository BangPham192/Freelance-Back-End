package com.backend.freelance.controllers;

import com.backend.freelance.dtos.AuthTokenDto;
import com.backend.freelance.dtos.UserDto;
import com.backend.freelance.interfaces.IAuthenticationController;
import com.backend.freelance.mapper.UserMapper;
import com.backend.freelance.models.Role;
import com.backend.freelance.models.User;
import com.backend.freelance.models.UserRole;
import com.backend.freelance.request.LoginRequest;
import com.backend.freelance.request.UserCreateRequest;
import com.backend.freelance.services.JwtService;
import com.backend.freelance.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController implements IAuthenticationController {
    private final UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public String login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid username/password supplied");
        }
        return jwtService.generateToken(request.getUsername());
    }

    @Override
    public ResponseEntity<Void> createUser(@Valid UserCreateRequest request) {
        try {
            // check existing user
            User existingUser = userService.getUserByEmail(request.getEmail());
            if (existingUser != null && existingUser.getRoles().toString().equals(request.getRole())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public UserDto getMyself() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userService.getMySelf(userDetails.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user information: " + e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An error occurred: " + e.getMessage());
    }
}
