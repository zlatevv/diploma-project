package com.zlatev.tournament_management_system.service.fixture;

import com.zlatev.tournament_management_system.dto.fixture.GenerateFixturesRequestDTO;
import com.zlatev.tournament_management_system.dto.match.MatchResponseDTO;
import com.zlatev.tournament_management_system.entity.enums.tournament.TournamentStatus;
import com.zlatev.tournament_management_system.entity.model.fixture.Fixture;
import com.zlatev.tournament_management_system.entity.model.group.Group;
import com.zlatev.tournament_management_system.entity.model.match.Match;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import com.zlatev.tournament_management_system.entity.model.tournament.Tournament;
import com.zlatev.tournament_management_system.exception.tournament.NotEnoughTeamsException;
import com.zlatev.tournament_management_system.exception.tournament.TournamentNotFoundException;
import com.zlatev.tournament_management_system.mapper.match.MatchMapper;
import com.zlatev.tournament_management_system.repository.group.GroupRepository;
import com.zlatev.tournament_management_system.repository.match.MatchRepository;
import com.zlatev.tournament_management_system.repository.team.TeamRepository;
import com.zlatev.tournament_management_system.repository.tournament.TournamentRepository;
import com.zlatev.tournament_management_system.scheduler.Generator;
import com.zlatev.tournament_management_system.scheduler.groupplayoff.GroupsPlayoffConfig;
import com.zlatev.tournament_management_system.scheduler.groupplayoff.GroupsPlayoffGenerator;
import com.zlatev.tournament_management_system.scheduler.knockout.KnockoutGenerator;
import com.zlatev.tournament_management_system.scheduler.roundrobin.RoundRobinGenerator;
import com.zlatev.tournament_management_system.service.tournament.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FixtureService {
    private final TournamentService tournamentService;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final GroupRepository groupRepository;

    @Transactional
    @CacheEvict(value = {"tournaments", "tournament", "tournamentTeams"}, allEntries = true)
    public List<MatchResponseDTO> generateFixtures(UUID tournamentId,
                                                   GenerateFixturesRequestDTO request,
                                                   String username) {
        Tournament tournament = tournamentService.findOwnedDraftTournament(tournamentId, username);

        List<Team> teams = teamRepository.findByTournamentId(tournamentId);
        if (teams.size() < 2) {
            throw new NotEnoughTeamsException("A tournament needs at least 2 teams!");
        }

        boolean playsTwice = request == null
                || (request.playsEachOtherTwice() == null && request.doubleRoundRobin() == null)
                || Boolean.TRUE.equals(request.playsEachOtherTwice())
                || Boolean.TRUE.equals(request.doubleRoundRobin());

        Generator generator = switch (tournament.getFormat()) {
            case LEAGUE -> new RoundRobinGenerator(playsTwice);
            case KNOCKOUT -> new KnockoutGenerator();
            case GROUPS_PLAYOFF -> new GroupsPlayoffGenerator(buildGroupsConfig(request, tournament));
        };

        List<Fixture> fixtures = generator.generate(teams);

        List<Group> groups = fixtures.stream()
                .map(Fixture::group)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (!groups.isEmpty()) {
            groupRepository.saveAll(groups);
        }

        List<Match> matches = fixtures.stream()
                .map(f -> MatchMapper.toMatch(f, tournament))
                .toList();
        matchRepository.saveAll(matches);

        tournament.setStatus(TournamentStatus.ONGOING);
        tournamentRepository.save(tournament);

        return matches.stream().map(MatchMapper::toResponse).toList();
    }

    private GroupsPlayoffConfig buildGroupsConfig(GenerateFixturesRequestDTO request, Tournament tournament) {
        if (request == null || request.numberOfGroups() == null
                || request.teamsPerGroup() == null || request.qualifyingTeamsPerGroup() == null) {
            throw new IllegalArgumentException(
                    "numberOfGroups, teamsPerGroup and qualifyingTeamsPerGroup are required for GROUPS_PLAYOFF!");
        }
        if (request.qualifyingTeamsPerGroup() > request.teamsPerGroup()) {
            throw new IllegalArgumentException("Qualifying teams can't exceed teams per group!");
        }
        int totalQualifiers = request.numberOfGroups() * request.qualifyingTeamsPerGroup();
        if (!KnockoutGenerator.isPowerOfTwo(totalQualifiers)) {
            throw new IllegalArgumentException("Total qualifiers must be 2, 4, 8, 16 etc. for the playoff!");
        }

        boolean twice = Boolean.TRUE.equals(request.doubleRoundRobin());
        return new GroupsPlayoffConfig(request.numberOfGroups(), request.qualifyingTeamsPerGroup(),
                request.teamsPerGroup(), twice, tournament);
    }

    @Transactional(readOnly = true)
    public List<MatchResponseDTO> getMatches(UUID tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new TournamentNotFoundException("Tournament does not exist!");
        }
        return matchRepository.findByTournamentIdOrderByRoundAsc(tournamentId).stream()
                .map(MatchMapper::toResponse)
                .toList();
    }
}
