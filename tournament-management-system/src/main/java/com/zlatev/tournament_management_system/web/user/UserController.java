package com.zlatev.tournament_management_system.web.user;

import com.zlatev.tournament_management_system.dto.user.LoginRequestDTO;
import com.zlatev.tournament_management_system.dto.user.LoginResponseDTO;
import com.zlatev.tournament_management_system.dto.user.RegisterRequestDTO;
import com.zlatev.tournament_management_system.dto.user.UserResponseDTO;
import com.zlatev.tournament_management_system.security.CustomUserDetails;
import com.zlatev.tournament_management_system.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(registerRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(userService.login(loginRequestDTO));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(userService.getCurrentUser(principal.getUserId()));
    }
}
