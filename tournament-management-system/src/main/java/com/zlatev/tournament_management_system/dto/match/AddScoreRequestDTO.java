package com.zlatev.tournament_management_system.dto.match;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record AddScoreRequestDTO(
        @NotNull UUID teamId,
        @NotNull UUID scoringTypeId,
        @PositiveOrZero Integer matchMinute)
{}
