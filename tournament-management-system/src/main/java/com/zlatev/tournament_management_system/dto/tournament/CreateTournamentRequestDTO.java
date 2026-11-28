package com.zlatev.tournament_management_system.dto.tournament;

import com.zlatev.tournament_management_system.entity.enums.tournament.TournamentFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateTournamentRequestDTO(
        @NotBlank String name,
        @NotNull(message = "Tournament format is required")
        TournamentFormat tournamentFormat,
        UUID sportId
) {
}
