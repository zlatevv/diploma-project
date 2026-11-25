package com.zlatev.tournament_management_system.dto.match;

import com.zlatev.tournament_management_system.entity.enums.match.MatchStatus;

import java.time.Instant;
import java.util.UUID;

public record MatchResponseDTO(
        UUID id, Integer round,
        UUID homeTeamId, String homeTeamName,
        UUID awayTeamId, String awayTeamName,
        Integer homeScore, Integer awayScore,
        MatchStatus status, Instant scheduledAt) {}
