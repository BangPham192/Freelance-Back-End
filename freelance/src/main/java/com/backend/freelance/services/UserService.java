package com.backend.freelance.services;

import com.backend.freelance.dtos.UserDto;
import com.backend.freelance.mapper.UserMapper;
import com.backend.freelance.models.Role;
import com.backend.freelance.models.User;
import com.backend.freelance.models.UserRole;
import com.backend.freelance.repository.UserRepository;
import com.backend.freelance.repository.UserRolesRepository;
import com.backend.freelance.request.UserCreateRequest;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRolesRepository userRolesRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       UserRolesRepository userRolesRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRolesRepository = userRolesRepository;
    }

    public void createUser(UserCreateRequest request) {
        User user = UserMapper.INSTANCE.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPublicId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        // create user role
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRoleName(Role.valueOf(request.getRole()));
        userRolesRepository.save(userRole);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDto getMySelf(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setPublicId(user.getPublicId());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUsername(user.getUsername());
        userDto.setRoles(user.getRoles().stream()
                .map(UserRole::getRoleName)
                .map(Role::name)
                .toList());
        return userDto;
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
