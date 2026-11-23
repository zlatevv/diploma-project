package com.zlatev.tournament_management_system.dto.tournament;

import com.zlatev.tournament_management_system.entity.enums.tournament.TournamentFormat;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateTournamentRequestDTO(
        @NotBlank String name,
        TournamentFormat tournamentFormat,
        UUID sportId
) {
}
