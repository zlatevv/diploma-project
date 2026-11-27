package com.zlatev.tournament_management_system.web.user;

import com.zlatev.tournament_management_system.dto.user.UpdateUserRoleRequestDTO;
import com.zlatev.tournament_management_system.entity.model.user.User;
import com.zlatev.tournament_management_system.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userRoleService;

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Serializable>> updateUserRole(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRoleRequestDTO request) {

        User updated = userRoleService.updateUserRole(userId, request.role());
        return ResponseEntity.ok(Map.of(
                "id", updated.getId(),
                "email", updated.getEmail(),
                "role", updated.getRole()
        ));
    }
}