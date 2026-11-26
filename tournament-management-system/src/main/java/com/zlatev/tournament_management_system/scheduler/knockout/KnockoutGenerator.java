package com.zlatev.tournament_management_system.scheduler.knockout;

import com.zlatev.tournament_management_system.entity.model.fixture.Fixture;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import com.zlatev.tournament_management_system.exception.tournament.InvalidNumberOfTeamsException;
import com.zlatev.tournament_management_system.scheduler.Generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KnockoutGenerator implements Generator {
    private final Random random = new Random();


    @Override
    public List<Fixture> generate(List<Team> teams) {
        List<Team> list = new ArrayList<>(teams);
        List<Fixture> fixtures = new ArrayList<>();

        int n = list.size();

        if (!isPowerOfTwo(n)) {
            throw new InvalidNumberOfTeamsException("Teams need to be 2, 4, 8 etc...");
        }

        Collections.shuffle(list);

        while (!list.isEmpty()) {
            int homeTeamIndex = random.nextInt(list.size());
            int awayTeamIndex = random.nextInt(list.size());

            if (homeTeamIndex == awayTeamIndex) {
                continue;
            }

            Team homeTeam = list.get(homeTeamIndex);
            Team awayTeam = list.get(awayTeamIndex);

            Fixture fixture = new Fixture(null, homeTeam, awayTeam, null);

            int firstIndex = Math.min(homeTeamIndex, awayTeamIndex);
            int secondIndex = Math.max(homeTeamIndex, awayTeamIndex);

            list.remove(secondIndex);
            list.remove(firstIndex);

            fixtures.add(fixture);
        }
        return fixtures;
    }

    public static boolean isPowerOfTwo(int n) {
        return n > 1 && (n & (n - 1)) == 0;
    }
}
