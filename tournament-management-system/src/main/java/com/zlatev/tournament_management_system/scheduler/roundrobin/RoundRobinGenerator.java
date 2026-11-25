package com.zlatev.tournament_management_system.scheduler.roundrobin;

import com.zlatev.tournament_management_system.entity.model.fixture.Fixture;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import com.zlatev.tournament_management_system.scheduler.Generator;

import java.util.ArrayList;
import java.util.List;

public class RoundRobinGenerator implements Generator {
    @Override
    public List<Fixture> generate(List<Team> teams) {
        List<Fixture> legs = generateLeg(teams);
        int roundsPerLeg = (teams.size() % 2 == 0 ? teams.size() : teams.size() + 1) - 1;

        List<Fixture> fixtures = new ArrayList<>(legs);
        for (Fixture f : legs) {
            fixtures.add(new Fixture(f.round() + roundsPerLeg, f.awayTeam(), f.homeTeam()));
        }
        return fixtures;
    }

    private List<Fixture> generateLeg(List<Team> teams) {
        List<Team> list = new ArrayList<>(teams);
        if (list.size() % 2 != 0) list.add(null);

        List<Fixture> fixtures = new ArrayList<>();

        int n = list.size();
        int rounds = n - 1;

        for (int round = 1; round <= rounds; round++){
            for (int i = 0; i < n / 2; i++) {
                Team homeTeam = list.get(i);
                Team awayTeam = list.get(n - i - 1);

                if (homeTeam != null && awayTeam != null) {
                    if (i == 0 && round % 2 == 0) {
                        fixtures.add(new Fixture(round, awayTeam, homeTeam));
                    } else {
                        fixtures.add(new Fixture(round, homeTeam, awayTeam));
                    }
                }
            }

            list.add(1, list.remove(n - 1));
        }
        return fixtures;
    }
}
