package com.zlatev.tournament_management_system.scheduler.knockout;

import com.zlatev.tournament_management_system.entity.model.fixture.Fixture;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import com.zlatev.tournament_management_system.scheduler.Generator;

import java.util.List;

public class KnockoutGenerator implements Generator {
    @Override
    public List<Fixture> generate(List<Team> teams) {
        return List.of();
    }
}
