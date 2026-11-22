package com.zlatev.tournament_management_system.dto.sport;

import jakarta.validation.constraints.NotBlank;

public record CreateSportRequestDTO(
        @NotBlank String sportName
) {
}
