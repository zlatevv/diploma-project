package com.zlatev.tournament_management_system.dto.sport;

import com.zlatev.tournament_management_system.dto.scoring.ScoringTypeResponseDTO;
import com.zlatev.tournament_management_system.entity.model.scoring.ScoringType;

import java.util.List;
import java.util.UUID;

public record SportResponseDTO(
        UUID sportId,
        String sportName,
        List<ScoringTypeResponseDTO> scoringTypes
) {
}
