package com.zlatev.tournament_management_system.dto.user;

import com.zlatev.tournament_management_system.entity.enums.user.Role;

import java.util.UUID;

public record UserResponseDTO (
        UUID id,
        String fullName,
        String email,
        Role role
){
}
