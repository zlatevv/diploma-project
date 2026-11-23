package com.zlatev.tournament_management_system.service.team;

import com.zlatev.tournament_management_system.dto.team.CreateTeamRequestDTO;
import com.zlatev.tournament_management_system.dto.team.TeamResponseDTO;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import com.zlatev.tournament_management_system.entity.model.tournament.Tournament;
import com.zlatev.tournament_management_system.exception.team.TeamAlreadyExistsException;
import com.zlatev.tournament_management_system.exception.team.TeamNotFoundException;
import com.zlatev.tournament_management_system.exception.tournament.TournamentNotFoundException;
import com.zlatev.tournament_management_system.mapper.team.TeamMapper;
import com.zlatev.tournament_management_system.repository.team.TeamRepository;
import com.zlatev.tournament_management_system.repository.tournament.TournamentRepository;
import com.zlatev.tournament_management_system.service.tournament.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final TournamentService tournamentService;

    @Transactional
    @CacheEvict(value = {"tournamentTeams", "tournaments", "tournament"}, allEntries = true)
    public TeamResponseDTO addTeam(UUID tournamentId, CreateTeamRequestDTO dto, String username) {
        Tournament tournament = tournamentService.findOwnedDraftTournament(tournamentId, username);
        String name = dto.name().trim();

        if (teamRepository.existsByNameAndTournamentId(name, tournamentId)) {
            throw new TeamAlreadyExistsException("Team already exists in this tournament!");
        }

        Team team = Team.builder().name(name).tournament(tournament).build();
        return TeamMapper.toResponse(teamRepository.save(team));
    }

    @Transactional
    @CacheEvict(value = {"tournamentTeams", "tournaments", "tournament"}, allEntries = true)
    public void removeTeam(UUID tournamentId, UUID teamId, String username) {
        tournamentService.findOwnedDraftTournament(tournamentId, username);

        Team team = teamRepository.findByIdAndTournamentId(teamId, tournamentId)
                .orElseThrow(() -> new TeamNotFoundException("Team does not exist in this tournament!"));
        teamRepository.delete(team);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tournamentTeams", key = "#tournamentId")
    public List<TeamResponseDTO> getTeams(UUID tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new TournamentNotFoundException("Tournament does not exist!");
        }
        return teamRepository.findByTournamentId(tournamentId).stream()
                .map(TeamMapper::toResponse)
                .toList();
    }
}
