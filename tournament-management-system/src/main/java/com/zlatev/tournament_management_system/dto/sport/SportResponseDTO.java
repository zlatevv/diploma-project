package com.zlatev.tournament_management_system.dto.sport;

import com.zlatev.tournament_management_system.entity.model.scoring.ScoringType;

import java.util.List;

public record SportResponseDTO(
        String sportName,
        List<ScoringType> scoringTypes
) {
}
