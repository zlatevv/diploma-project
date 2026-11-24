package com.zlatev.tournament_management_system.scheduler.roundrobbin;

import com.zlatev.tournament_management_system.entity.model.fixture.Fixture;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import com.zlatev.tournament_management_system.scheduler.Generator;

import java.util.ArrayList;
import java.util.List;

public class RoundRobbinGenerator implements Generator {
    @Override
    public List<Fixture> generate(List<Team> teams) {
        List<Fixture> fixtures = new ArrayList<>();

        fixtures.addAll(generateFirstVariant(teams));
        fixtures.addAll(generateSecondVariant(teams));
        return fixtures;
    }

    private List<Fixture> generateSecondVariant(List<Team> teams) {
        return null;
    }

    private List<Fixture> generateFirstVariant(List<Team> teams) {
        List<Team> list = new ArrayList<>(teams);
        if (list.size() % 2 != 0) list.add(null);

        List<Fixture> fixtures = new ArrayList<>();

        int n = teams.size();
        int rounds = n - 1;

        for (int round = 1; round <= rounds; round++){
            for (int i = 0; i < n; i++) {
                Team homeTeam = teams.get(i);
                Team awayTeam = teams.get(n - i - 1);

                if (homeTeam != null && awayTeam != null) {
                    fixtures.add(new Fixture(round, homeTeam, awayTeam));
                }
            }

            list.add(1, list.remove(n - 1));
        }
        return fixtures;
    }
}
