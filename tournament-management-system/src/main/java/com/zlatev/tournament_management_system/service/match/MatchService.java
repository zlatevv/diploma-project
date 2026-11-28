package com.zlatev.tournament_management_system.service.match;

import com.zlatev.tournament_management_system.dto.match.MatchResponseDTO;
import com.zlatev.tournament_management_system.dto.match.MatchUpdatedEvent;
import com.zlatev.tournament_management_system.entity.enums.match.MatchStatus;
import com.zlatev.tournament_management_system.entity.enums.tournament.TournamentStatus;
import com.zlatev.tournament_management_system.entity.enums.user.Role;
import com.zlatev.tournament_management_system.entity.model.match.Match;
import com.zlatev.tournament_management_system.entity.model.user.User;
import com.zlatev.tournament_management_system.exception.match.*;
import com.zlatev.tournament_management_system.exception.tournament.NotTournamentOwnerException;
import com.zlatev.tournament_management_system.exception.tournament.TournamentNotStartedException;
import com.zlatev.tournament_management_system.exception.user.InvalidRefereeException;
import com.zlatev.tournament_management_system.exception.user.UserNotFoundException;
import com.zlatev.tournament_management_system.mapper.match.MatchMapper;
import com.zlatev.tournament_management_system.repository.match.MatchRepository;
import com.zlatev.tournament_management_system.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public MatchResponseDTO assignReferee(UUID matchId, UUID refereeId, String callerEmail) {
        Match match = findMatch(matchId);

        if (!match.getTournament().getOrganizer().getEmail().equals(callerEmail)) {
            throw new NotTournamentOwnerException("You are not the organizer of this tournament!");
        }
        if (match.getStatus() == MatchStatus.FINISHED) {
            throw new MatchAlreadyFinishedException("Match has already finished!");
        }

        User referee = userRepository.findById(refereeId)
                .orElseThrow(() -> new UserNotFoundException("Referee does not exist!"));
        if (referee.getRole() != Role.REFEREE) {
            throw new InvalidRefereeException("This user is not a referee!");
        }

        match.setReferee(referee);

        MatchResponseDTO response = MatchMapper.toResponse(matchRepository.save(match));
        eventPublisher.publishEvent(
                new MatchUpdatedEvent(match.getId(), match.getTournament().getId(), response));
        return response;
    }

    @Transactional
    public MatchResponseDTO startMatch(UUID matchId, String callerEmail) {
        Match match = findMatch(matchId);
        checkCanOfficiate(match, callerEmail);
        requireTournamentOngoing(match);

        if (match.getStatus() == MatchStatus.FINISHED) {
            throw new MatchAlreadyFinishedException("Match has already finished!");
        }
        if (match.getStatus() != MatchStatus.SCHEDULED) {
            throw new InvalidMatchStateException("Match is already live!");
        }

        match.setStatus(MatchStatus.LIVE);

        MatchResponseDTO response = MatchMapper.toResponse(matchRepository.save(match));
        eventPublisher.publishEvent(
                new MatchUpdatedEvent(match.getId(), match.getTournament().getId(), response));
        return response;
    }

    @Transactional
    public MatchResponseDTO endMatch(UUID matchId, String callerEmail) {
        Match match = findMatch(matchId);
        checkCanOfficiate(match, callerEmail);
        requireTournamentOngoing(match);

        if (match.getStatus() == MatchStatus.FINISHED) {
            throw new MatchAlreadyFinishedException("Match has already finished!");
        }
        if (match.getStatus() != MatchStatus.LIVE) {
            throw new InvalidMatchStateException("Match has not started yet!");
        }

        match.setStatus(MatchStatus.FINISHED);

        MatchResponseDTO response = MatchMapper.toResponse(matchRepository.save(match));
        eventPublisher.publishEvent(
                new MatchUpdatedEvent(match.getId(), match.getTournament().getId(), response));
        return response;
    }

    @Transactional(readOnly = true)
    public MatchResponseDTO getMatch(UUID matchId) {
        return MatchMapper.toResponse(findMatch(matchId));
    }

    // ---------- helpers (the scoring service will reuse these) ----------

    Match findMatch(UUID matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("This match does not exist!"));
    }

    void checkCanOfficiate(Match match, String callerEmail) {
        boolean isReferee = match.getReferee() != null
                && match.getReferee().getEmail().equals(callerEmail);
        boolean isOrganizer = match.getTournament().getOrganizer().getEmail().equals(callerEmail);

        if (!isReferee && !isOrganizer) {
            throw new NotMatchOfficialException("You are not allowed to manage this match!");
        }
    }

    private void requireTournamentOngoing(Match match) {
        if (match.getTournament().getStatus() != TournamentStatus.ONGOING) {
            throw new TournamentNotStartedException("Tournament is not ongoing!");
        }
    }
}
