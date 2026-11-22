package com.zlatev.tournament_management_system.dto.scoring;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ScoringTypeResponseDTO(
        @NotBlank String name,
        @Positive Integer points,
        UUID sportId
) {
}
