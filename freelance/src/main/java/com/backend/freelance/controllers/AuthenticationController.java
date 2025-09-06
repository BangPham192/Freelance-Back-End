package com.backend.freelance.controllers;

import com.backend.freelance.dao.impl.RefreshTokenDaoImpl;
import com.backend.freelance.dtos.AuthTokenDto;
import com.backend.freelance.dtos.UserDto;
import com.backend.freelance.interfaces.IAuthenticationController;
import com.backend.freelance.models.User;
import com.backend.freelance.request.ChangePassWordRequest;
import com.backend.freelance.request.LoginRequest;
import com.backend.freelance.request.RefreshTokenRequest;
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
public class AuthenticationController extends BaseController implements IAuthenticationController {
    private final UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenDaoImpl refreshTokenDao;

    @Autowired
    public AuthenticationController(UserService userService, JwtService jwtService, RefreshTokenDaoImpl refreshTokenDao) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenDao = refreshTokenDao;
    }

    @Override
    public AuthTokenDto login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            AuthTokenDto authTokenDto = new AuthTokenDto();
            authTokenDto.setAccessToken(jwtService.generateToken(request.getUsername(), 5 * 60 * 1000)); // 5 minutes in milliseconds
            authTokenDto.setRefreshToken(jwtService.generateToken(request.getUsername(), 7L * 24 * 60 * 60 * 1000)); // 7 days in milliseconds
            // Store the refresh token in Redis
            refreshTokenDao.storeRefreshToken(request.getUsername(), authTokenDto.getRefreshToken());
            return authTokenDto;
        } catch (Exception e) {
            throw new RuntimeException("Error during login: " + e.getMessage());
        }
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

    @Override
    public AuthTokenDto refreshToken(RefreshTokenRequest request) {
        try {
            String token = request.getRefreshToken();
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("Refresh token is required");
            }
            String username = jwtService.extractUsername(token);
            // add logic validate token form redis
            String refreshTokenFromRedis = refreshTokenDao.getRefreshTokenByUsername(username);
            if (refreshTokenFromRedis == null || !refreshTokenFromRedis.equals(token)) {
                throw new RuntimeException("Invalid or expired refresh token");
            }

            User user = userService.getUserByEmail(username);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            // Generate a new token with a validity of 7 days
            String newToken = jwtService.generateToken(username, 5 * 60 * 1000); // 5 minutes in milliseconds
            String newRefreshToken = jwtService.generateToken(username, 7L * 24 * 60 * 60 * 1000); // 7 days in milliseconds
            AuthTokenDto authTokenDto = new AuthTokenDto();
            authTokenDto.setAccessToken(newToken);
            authTokenDto.setRefreshToken(newRefreshToken); // Keep the same refresh token
            return authTokenDto;
        } catch (Exception e) {
            throw new RuntimeException("Error refreshing token: " + e.getMessage());
        }
    }

    @Override
    public void changePassword(ChangePassWordRequest request) {
        try {
            String username = getCurrentUser().getUsername();
            userService.changePassword(username, request.getOldPassword(), request.getNewPassword());
        } catch (Exception e) {
            throw new RuntimeException("Error changing password: " + e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An error occurred: " + e.getMessage());
    }
}
