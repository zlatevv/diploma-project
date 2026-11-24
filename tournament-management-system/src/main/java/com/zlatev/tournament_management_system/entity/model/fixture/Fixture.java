package com.zlatev.tournament_management_system.entity.model.fixture;

import com.zlatev.tournament_management_system.entity.model.team.Team;

public record Fixture(
        int round,
        Team homeTeam,
        Team awayTeam
) {
}
