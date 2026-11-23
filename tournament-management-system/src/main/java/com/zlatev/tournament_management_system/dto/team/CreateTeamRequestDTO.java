package com.zlatev.tournament_management_system.dto.team;

import jakarta.validation.constraints.NotBlank;

public record CreateTeamRequestDTO(
        @NotBlank String name
) {
}
