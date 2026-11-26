package com.zlatev.tournament_management_system.scheduler.groupplayoff;

import com.zlatev.tournament_management_system.entity.model.fixture.Fixture;
import com.zlatev.tournament_management_system.entity.model.group.Group;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import com.zlatev.tournament_management_system.scheduler.Generator;
import com.zlatev.tournament_management_system.scheduler.roundrobin.RoundRobinGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupsPlayoffGenerator implements Generator {
    private final RoundRobinGenerator roundRobinGenerator;
    private final GroupsPlayoffConfig config;

    public GroupsPlayoffGenerator(GroupsPlayoffConfig config) {
        this.config = config;
        this.roundRobinGenerator = new RoundRobinGenerator(config.teamsPlayEachOtherTwice());
    }

    @Override
    public List<Fixture> generate(List<Team> teams) {
        if (teams.size() != config.numberOfGroups() * config.teamsPerGroup()) {
            throw new IllegalArgumentException("Team count doesn't match group configuration!");
        }

        List<Team> shuffledTeams = new ArrayList<>(teams);
        Collections.shuffle(shuffledTeams);

        List<Group> groups = generateGroups(shuffledTeams);
        return generateFixtures(groups);
    }

    private List<Fixture> generateFixtures(List<Group> groups) {
        List<Fixture> allFixtures = new ArrayList<>();

        for (Group group : groups) {
            roundRobinGenerator.generate(group.getTeams()).stream()
                    .map(f -> new Fixture(f.round(), f.homeTeam(), f.awayTeam(), group))
                    .forEach(allFixtures::add);
        }
        return allFixtures;
    }

    private List<Group> generateGroups(List<Team> teams) {
        List<Group> groups = new ArrayList<>();

        for (int i = 0; i < config.numberOfGroups(); i++) {
            Group group = new Group();
            group.setTournament(config.tournament());
            group.setName(String.valueOf((char) ('A' + i)));

            for (int j = 0; j < config.teamsPerGroup(); j++) {
                group.getTeams().add(teams.remove(0));
            }
            groups.add(group);
        }
        return groups;
    }
}
