package com.zlatev.tournament_management_system.service.fixture;

import com.zlatev.tournament_management_system.dto.match.MatchResponseDTO;
import com.zlatev.tournament_management_system.entity.enums.tournament.TournamentStatus;
import com.zlatev.tournament_management_system.entity.model.match.Match;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import com.zlatev.tournament_management_system.entity.model.tournament.Tournament;
import com.zlatev.tournament_management_system.exception.tournament.NotEnoughTeamsException;
import com.zlatev.tournament_management_system.exception.tournament.TournamentNotFoundException;
import com.zlatev.tournament_management_system.exception.tournament.UnsupportedTournamentFormatException;
import com.zlatev.tournament_management_system.mapper.match.MatchMapper;
import com.zlatev.tournament_management_system.repository.match.MatchRepository;
import com.zlatev.tournament_management_system.repository.team.TeamRepository;
import com.zlatev.tournament_management_system.repository.tournament.TournamentRepository;
import com.zlatev.tournament_management_system.scheduler.Generator;
import com.zlatev.tournament_management_system.scheduler.roundrobin.RoundRobinGenerator;
import com.zlatev.tournament_management_system.service.tournament.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.zlatev.tournament_management_system.entity.enums.tournament.TournamentFormat.LEAGUE;

@Service
@RequiredArgsConstructor
public class FixtureService {
    private final TournamentService tournamentService;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    @Transactional
    @CacheEvict(value = {"tournaments", "tournament", "tournamentTeams"}, allEntries = true)
    public List<MatchResponseDTO> generateFixtures(UUID tournamentId, String username) {
        Tournament tournament = tournamentService.findOwnedDraftTournament(tournamentId, username);

        List<Team> teams = teamRepository.findByTournamentId(tournamentId);
        if (teams.size() < 2) {
            throw new NotEnoughTeamsException("A tournament needs at least 2 teams!");
        }

        Generator generator = switch (tournament.getFormat()) {
            case LEAGUE -> new RoundRobinGenerator();
            default -> throw new UnsupportedTournamentFormatException(
                    "Format not supported yet: " + tournament.getFormat());
        };

        List<Match> matches = generator.generate(teams).stream()
                .map(f -> MatchMapper.toMatch(f, tournament))
                .toList();

        matchRepository.saveAll(matches);

        tournament.setStatus(TournamentStatus.ONGOING);
        tournamentRepository.save(tournament);

        return matches.stream().map(MatchMapper::toResponse).toList();
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
