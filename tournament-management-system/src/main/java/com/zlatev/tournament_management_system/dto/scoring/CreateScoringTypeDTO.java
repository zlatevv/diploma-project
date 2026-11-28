package com.zlatev.tournament_management_system.dto.scoring;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateScoringTypeDTO(
        @NotBlank String name,
        @NotNull @Positive Integer points
) {
}
