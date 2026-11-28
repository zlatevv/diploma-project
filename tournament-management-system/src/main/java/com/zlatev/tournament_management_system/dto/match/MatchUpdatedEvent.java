package com.zlatev.tournament_management_system.dto.match;

import java.util.UUID;

public record MatchUpdatedEvent(
        UUID matchId,
        UUID tournamentId,
        MatchResponseDTO match
) {}
