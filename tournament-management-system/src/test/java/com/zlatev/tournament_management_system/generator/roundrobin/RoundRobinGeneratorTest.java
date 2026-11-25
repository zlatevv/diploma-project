package com.zlatev.tournament_management_system.generator.roundrobin;

import com.zlatev.tournament_management_system.entity.model.fixture.Fixture;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import com.zlatev.tournament_management_system.scheduler.roundrobin.RoundRobinGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoundRobinGeneratorTest {

    private final RoundRobinGenerator generator = new RoundRobinGenerator();

    private List<Team> teams(int count) {
        List<Team> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(Team.builder().id(UUID.randomUUID()).name("Team " + i).build());
        }
        return list;
    }

    @ParameterizedTest
    @CsvSource({"2,2,2", "4,12,6", "5,20,10", "6,30,10"})   // teams, fixtures, rounds
    void generatesCorrectCounts(int teamCount, int expectedFixtures, int expectedRounds) {
        List<Fixture> fixtures = generator.generate(teams(teamCount));

        assertEquals(expectedFixtures, fixtures.size());
        assertEquals(expectedRounds, fixtures.stream().mapToInt(Fixture::round).max().orElse(0));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5, 6, 8})
    void everyOrderedPairAppearsExactlyOnce(int teamCount) {
        List<Fixture> fixtures = generator.generate(teams(teamCount));

        Set<String> pairs = new HashSet<>();
        for (Fixture f : fixtures) {
            assertTrue(pairs.add(f.homeTeam().getId() + ">" + f.awayTeam().getId()), "duplicate fixture");
        }
        assertEquals(teamCount * (teamCount - 1), pairs.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 6, 8})
    void noTeamPlaysTwiceInTheSameRound(int teamCount) {
        List<Fixture> fixtures = generator.generate(teams(teamCount));

        Map<Integer, List<Fixture>> byRound = fixtures.stream().collect(Collectors.groupingBy(Fixture::round));
        byRound.forEach((round, list) -> {
            Set<UUID> seen = new HashSet<>();
            for (Fixture f : list) {
                assertTrue(seen.add(f.homeTeam().getId()), "team twice in round " + round);
                assertTrue(seen.add(f.awayTeam().getId()), "team twice in round " + round);
            }
        });
    }
}