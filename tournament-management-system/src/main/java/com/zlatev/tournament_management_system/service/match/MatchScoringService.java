package com.zlatev.tournament_management_system.service.match;

import com.zlatev.tournament_management_system.dto.match.AddScoreRequestDTO;
import com.zlatev.tournament_management_system.dto.match.MatchEventResponseDTO;
import com.zlatev.tournament_management_system.dto.match.MatchResponseDTO;
import com.zlatev.tournament_management_system.dto.match.MatchUpdatedEvent;
import com.zlatev.tournament_management_system.entity.enums.match.MatchStatus;
import com.zlatev.tournament_management_system.entity.model.match.Match;
import com.zlatev.tournament_management_system.entity.model.match.MatchEvent;
import com.zlatev.tournament_management_system.entity.model.scoring.ScoringType;
import com.zlatev.tournament_management_system.entity.model.team.Team;
import com.zlatev.tournament_management_system.exception.match.MatchNotFoundException;
import com.zlatev.tournament_management_system.exception.match.MatchNotLiveException;
import com.zlatev.tournament_management_system.exception.scoring.NoScoreToUndoException;
import com.zlatev.tournament_management_system.exception.team.TeamNotInMatchException;
import com.zlatev.tournament_management_system.exception.scoring.InvalidScoringData;
import com.zlatev.tournament_management_system.exception.scoring.ScoringTypeNotFoundException;
import com.zlatev.tournament_management_system.mapper.match.MatchMapper;
import com.zlatev.tournament_management_system.repository.match.MatchEventRepository;
import com.zlatev.tournament_management_system.repository.match.MatchRepository;
import com.zlatev.tournament_management_system.repository.scoring.ScoringTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchScoringService {
    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;
    private final ScoringTypeRepository scoringTypeRepository;
    private final MatchService matchService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public MatchResponseDTO addScore(UUID matchId, AddScoreRequestDTO request, String callerEmail) {
        Match match = lockMatch(matchId);
        matchService.checkCanOfficiate(match, callerEmail);
        requireLive(match);

        Team team = resolveTeam(match, request.teamId());

        ScoringType scoringType = scoringTypeRepository.findById(request.scoringTypeId())
                .orElseThrow(() -> new ScoringTypeNotFoundException("Scoring type does not exist!"));

        UUID matchSportId = match.getTournament().getSport().getId();
        if (!scoringType.getSport().getId().equals(matchSportId)) {
            throw new InvalidScoringData("This scoring type does not belong to the match's sport!");
        }

        MatchEvent event = MatchEvent.builder()
                .match(match)
                .team(team)
                .scoringType(scoringType)
                .matchMinute(request.matchMinute())
                .build();
        matchEventRepository.save(event);

        recalculateScore(match);

        MatchResponseDTO response = MatchMapper.toResponse(match);
        eventPublisher.publishEvent(
                new MatchUpdatedEvent(match.getId(), match.getTournament().getId(), response));
        return response;
    }

    @Transactional
    public MatchResponseDTO undoLastScore(UUID matchId, String callerEmail) {
        Match match = lockMatch(matchId);
        matchService.checkCanOfficiate(match, callerEmail);
        requireLive(match);

        MatchEvent last = matchEventRepository.findTopByMatchIdOrderByCreatedAtDesc(matchId)
                .orElseThrow(() -> new NoScoreToUndoException("There is nothing to undo!"));
        matchEventRepository.delete(last);

        recalculateScore(match);

        MatchResponseDTO response = MatchMapper.toResponse(match);
        eventPublisher.publishEvent(
                new MatchUpdatedEvent(match.getId(), match.getTournament().getId(), response));
        return response;
    }

    @Transactional(readOnly = true)
    public List<MatchEventResponseDTO> getEvents(UUID matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new MatchNotFoundException("This match does not exist!");
        }
        return matchEventRepository.findByMatchIdOrderByCreatedAtAsc(matchId).stream()
                .map(MatchMapper::toEventResponse)
                .toList();
    }

    // ---------- helpers ----------

    private Match lockMatch(UUID matchId) {
        return matchRepository.findByIdForUpdate(matchId)
                .orElseThrow(() -> new MatchNotFoundException("This match does not exist!"));
    }

    private void requireLive(Match match) {
        if (match.getStatus() != MatchStatus.LIVE) {
            throw new MatchNotLiveException("Scores can only be changed while the match is live!");
        }
    }

    private Team resolveTeam(Match match, UUID teamId) {
        if (match.getHomeTeam().getId().equals(teamId)) return match.getHomeTeam();
        if (match.getAwayTeam().getId().equals(teamId)) return match.getAwayTeam();
        throw new TeamNotInMatchException("This team is not playing in this match!");
    }

    private void recalculateScore(Match match) {
        match.setHomeScore(matchEventRepository.sumPoints(match.getId(), match.getHomeTeam().getId()));
        match.setAwayScore(matchEventRepository.sumPoints(match.getId(), match.getAwayTeam().getId()));
        // match is a managed entity inside the transaction, so this is saved at commit
    }
}
