package com.zlatev.tournament_management_system.dto.scoring;

import java.util.UUID;

public record ScoringTypeResponseDTO(
        UUID id,
        String name,
        Integer points,
        UUID sportId
) {
}
