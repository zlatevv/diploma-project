package com.zlatev.tournament_management_system.dto.user;

public record LoginResponseDTO(
        String token,
        UserResponseDTO user
) {
}