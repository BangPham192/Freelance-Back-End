package com.backend.freelance.interfaces;

import com.backend.freelance.dtos.AuthTokenDto;
import com.backend.freelance.dtos.UserDto;
import com.backend.freelance.request.LoginRequest;
import com.backend.freelance.request.UserCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public interface IAuthenticationController {

    @PostMapping("/login")
    String login(@RequestBody LoginRequest request);

    @PostMapping("/users")
    ResponseEntity<Void> createUser(@RequestBody @Validated UserCreateRequest request);

    @GetMapping("/myself")
    UserDto getMyself();

}
