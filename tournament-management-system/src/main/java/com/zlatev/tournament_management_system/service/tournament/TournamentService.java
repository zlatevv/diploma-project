package com.zlatev.tournament_management_system.service.tournament;

import com.zlatev.tournament_management_system.dto.tournament.CreateTournamentRequestDTO;
import com.zlatev.tournament_management_system.dto.tournament.TournamentResponseDTO;
import com.zlatev.tournament_management_system.entity.enums.tournament.TournamentStatus;
import com.zlatev.tournament_management_system.entity.model.sport.Sport;
import com.zlatev.tournament_management_system.entity.model.tournament.Tournament;
import com.zlatev.tournament_management_system.entity.model.user.User;
import com.zlatev.tournament_management_system.exception.sport.SportDoesNotExistException;
import com.zlatev.tournament_management_system.exception.tournament.NotTournamentOwnerException;
import com.zlatev.tournament_management_system.exception.tournament.TournamentNotEditableException;
import com.zlatev.tournament_management_system.exception.tournament.TournamentNotFoundException;
import com.zlatev.tournament_management_system.exception.user.UserNotFoundException;
import com.zlatev.tournament_management_system.mapper.tournament.TournamentMapper;
import com.zlatev.tournament_management_system.repository.sport.SportRepository;
import com.zlatev.tournament_management_system.repository.team.TeamRepository;
import com.zlatev.tournament_management_system.repository.tournament.TournamentRepository;
import com.zlatev.tournament_management_system.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final SportRepository sportRepository;
    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(value = {"tournaments", "tournament"}, allEntries = true)
    public TournamentResponseDTO createTournament(CreateTournamentRequestDTO dto, String email) {
        Sport sport = sportRepository.findById(dto.sportId())
                .orElseThrow(() -> new SportDoesNotExistException("Sport does not exist!"));
        User organizer = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        Tournament saved = tournamentRepository.save(TournamentMapper.toTournament(dto, sport, organizer));
        return TournamentMapper.toResponse(saved, 0);
    }

    @Transactional(readOnly = true)
    @Cacheable("tournaments")
    public List<TournamentResponseDTO> getAllTournaments() {
        return tournamentRepository.findAll().stream()
                .map(t -> TournamentMapper.toResponse(t, teamRepository.countByTournamentId(t.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tournament", key = "#tournamentId")
    public TournamentResponseDTO getTournamentById(UUID tournamentId) {
        Tournament t = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament does not exist!"));
        return TournamentMapper.toResponse(t, teamRepository.countByTournamentId(tournamentId));
    }

    @Transactional
    @CacheEvict(value = {"tournaments", "tournament"}, allEntries = true)
    public TournamentResponseDTO updateTournament(UUID tournamentId, CreateTournamentRequestDTO dto, String username) {
        Tournament tournament = findOwnedDraftTournament(tournamentId, username);

        Sport sport = sportRepository.findById(dto.sportId())
                .orElseThrow(() -> new SportDoesNotExistException("Sport does not exist!"));

        tournament.setName(dto.name().trim());
        tournament.setFormat(dto.tournamentFormat());
        tournament.setSport(sport);

        Tournament saved = tournamentRepository.save(tournament);
        return TournamentMapper.toResponse(saved, teamRepository.countByTournamentId(tournamentId));
    }

    @Transactional
    @CacheEvict(value = {"tournaments", "tournament", "tournamentTeams"}, allEntries = true)
    public void deleteTournament(UUID tournamentId, String username) {
        Tournament tournament = findOwnedDraftTournament(tournamentId, username);
        teamRepository.deleteByTournamentId(tournamentId);
        tournamentRepository.delete(tournament);
    }

    public Tournament findOwnedDraftTournament(UUID tournamentId, String email) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("Tournament does not exist!"));

        if (!tournament.getOrganizer().getEmail().equals(email)) {
            throw new NotTournamentOwnerException("You are not the organizer of this tournament!");
        }
        if (tournament.getStatus() != TournamentStatus.DRAFT) {
            throw new TournamentNotEditableException("Tournament can only be changed while in DRAFT!");
        }
        return tournament;
    }
}
