package com.zlatev.tournament_management_system.entity.model.fixture;

import com.zlatev.tournament_management_system.entity.model.group.Group;
import com.zlatev.tournament_management_system.entity.model.team.Team;

public record Fixture(
        Integer round,
        Team homeTeam,
        Team awayTeam,
        Group group
) {
}
