package com.zlatev.tournament_management_system.mapper.sport;

import com.zlatev.tournament_management_system.dto.sport.CreateSportRequestDTO;
import com.zlatev.tournament_management_system.dto.sport.SportResponseDTO;
import com.zlatev.tournament_management_system.entity.model.sport.Sport;

import java.util.ArrayList;

public class SportMapper {
    public static SportResponseDTO toSportResponseDTO(Sport sport) {
        return new SportResponseDTO(
                sport.getId(),
                sport.getSportName(),
                sport.getScoringTypes()
        );
    }

    public static Sport toSport(CreateSportRequestDTO createSportRequestDTO) {
        return Sport.builder()
                .sportName(createSportRequestDTO.sportName())
                .scoringTypes(new ArrayList<>())
                .build();
    }
}
