package com.zlatev.tournament_management_system.scheduler;

import com.zlatev.tournament_management_system.entity.model.fixture.Fixture;
import com.zlatev.tournament_management_system.entity.model.team.Team;

import java.util.List;

public interface Generator {
    List<Fixture> generate(List<Team> teams);
}
