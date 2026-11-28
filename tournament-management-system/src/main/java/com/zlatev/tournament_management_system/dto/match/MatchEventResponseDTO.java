package com.zlatev.tournament_management_system.dto.match;

import java.time.Instant;
import java.util.UUID;

public record MatchEventResponseDTO(
        UUID id, UUID teamId, String teamName,
        String scoringTypeName, Integer points,
        Integer matchMinute, Instant createdAt) {}