package com.zlatev.tournament_management_system.mapper.scoring;

import com.zlatev.tournament_management_system.dto.scoring.CreateScoringTypeDTO;
import com.zlatev.tournament_management_system.dto.scoring.ScoringTypeResponseDTO;
import com.zlatev.tournament_management_system.entity.model.scoring.ScoringType;

public class ScoringMapper {
    public static ScoringType toScoringType(CreateScoringTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        return ScoringType.builder()
                .name(dto.name())
                .points(dto.points())
                .build();
    }

    public static ScoringTypeResponseDTO toScoringResponse(ScoringType scoringType) {
        return new ScoringTypeResponseDTO(
                scoringType.getName(),
                scoringType.getPoints(),
                scoringType.getId()
        );
    }
}
