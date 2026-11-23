package com.zlatev.tournament_management_system.dto.team;

import java.util.UUID;

public record TeamResponseDTO(
        UUID id,
        String name
) {
}
