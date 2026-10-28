package com.zlatev.tournament_management_system.service.user;

import com.zlatev.tournament_management_system.dto.user.LoginRequestDTO;
import com.zlatev.tournament_management_system.dto.user.LoginResponseDTO;
import com.zlatev.tournament_management_system.dto.user.RegisterRequestDTO;
import com.zlatev.tournament_management_system.dto.user.UserResponseDTO;
import com.zlatev.tournament_management_system.entity.model.user.User;
import com.zlatev.tournament_management_system.exception.user.UserAlreadyExistsException;
import com.zlatev.tournament_management_system.exception.user.UserNotFoundException;
import com.zlatev.tournament_management_system.mapper.user.UserMapper;
import com.zlatev.tournament_management_system.repository.user.UserRepository;
import com.zlatev.tournament_management_system.security.CustomUserDetails;
import com.zlatev.tournament_management_system.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    @CacheEvict(value = "allUsers", key = "'all'")
    public UserResponseDTO register(RegisterRequestDTO registerRequest) {
        String normalizedEmail = registerRequest.email().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        User user = UserMapper.toUserEntity(registerRequest, normalizedEmail);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        return UserMapper.toUserResponse(savedUser);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(principal.getUsername(), principal.getRole().name());

        UserResponseDTO userResponse = new UserResponseDTO(
                principal.getUsername(),
                principal.getFullName(),
                principal.getEmail(),
                principal.getRole()
        );

        return new LoginResponseDTO(token, userResponse);
    }

    public UserResponseDTO getCurrentUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found!"));
        return UserMapper.toUserResponse(user);
    }
}
