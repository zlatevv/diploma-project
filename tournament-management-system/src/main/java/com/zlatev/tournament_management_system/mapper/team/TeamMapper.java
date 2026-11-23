package com.zlatev.tournament_management_system.mapper.team;

import com.zlatev.tournament_management_system.dto.team.TeamResponseDTO;
import com.zlatev.tournament_management_system.entity.model.team.Team;

public class TeamMapper {
    public static TeamResponseDTO toResponse(Team team) {
        return new TeamResponseDTO(team.getId(), team.getName());
    }
}
