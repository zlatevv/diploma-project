package com.zlatev.tournament_management_system.dto.sport;

import com.zlatev.tournament_management_system.entity.model.scoring.ScoringType;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record SportResponseDTO(
        @NotBlank String sportName,
        List<ScoringType> scoringTypes
) {
}
