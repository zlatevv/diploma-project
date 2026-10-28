package com.zlatev.tournament_management_system.dto.user;

import com.zlatev.tournament_management_system.entity.enums.user.Role;

public record UserResponseDTO (
        String username,
        String fullName,
        String email,
        Role role
){
}
