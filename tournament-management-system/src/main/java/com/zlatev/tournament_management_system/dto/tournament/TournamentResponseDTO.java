package com.zlatev.tournament_management_system.dto.tournament;

import com.zlatev.tournament_management_system.entity.enums.tournament.TournamentFormat;
import com.zlatev.tournament_management_system.entity.enums.tournament.TournamentStatus;

import java.util.UUID;

public record TournamentResponseDTO(
        UUID id,
        String name,
        TournamentFormat tournamentFormat,
        TournamentStatus status,
        UUID sportId,
        String sportName,
        String organizerName,
        Integer teamCount
) {
}
