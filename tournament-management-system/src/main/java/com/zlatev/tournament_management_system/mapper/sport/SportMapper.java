package com.zlatev.tournament_management_system.mapper.sport;

import com.zlatev.tournament_management_system.dto.scoring.ScoringTypeResponseDTO;
import com.zlatev.tournament_management_system.dto.sport.CreateSportRequestDTO;
import com.zlatev.tournament_management_system.dto.sport.SportResponseDTO;
import com.zlatev.tournament_management_system.entity.model.sport.Sport;
import com.zlatev.tournament_management_system.mapper.scoring.ScoringMapper;

import java.util.ArrayList;
import java.util.List;

public class SportMapper {
    public static SportResponseDTO toSportResponseDTO(Sport sport) {
        if (sport == null) {
            return null;
        }

        List<ScoringTypeResponseDTO> scoringTypes = sport.getScoringTypes() == null
                ? List.of()
                : sport.getScoringTypes().stream()
                .map(ScoringMapper::toScoringResponse)
                .toList();

        return new SportResponseDTO(
                sport.getId(),
                sport.getSportName(),
                scoringTypes
        );
    }

    public static Sport toSport(CreateSportRequestDTO createSportRequestDTO) {
        return Sport.builder()
                .sportName(createSportRequestDTO.sportName())
                .scoringTypes(new ArrayList<>())
                .build();
    }
}
