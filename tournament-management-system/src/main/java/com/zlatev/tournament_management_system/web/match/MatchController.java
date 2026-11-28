package com.zlatev.tournament_management_system.web.match;

import com.zlatev.tournament_management_system.dto.match.AddScoreRequestDTO;
import com.zlatev.tournament_management_system.dto.match.MatchEventResponseDTO;
import com.zlatev.tournament_management_system.dto.match.MatchResponseDTO;
import com.zlatev.tournament_management_system.dto.user.AssignRefereeRequestDTO;
import com.zlatev.tournament_management_system.service.match.MatchScoringService;
import com.zlatev.tournament_management_system.service.match.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final MatchScoringService matchScoringService;

    @GetMapping("/{matchId}")
    public MatchResponseDTO getMatch(@PathVariable UUID matchId) {
        return matchService.getMatch(matchId);
    }

    @PutMapping("/{matchId}/referee")
    @PreAuthorize("hasRole('ORGANIZER')")
    public MatchResponseDTO assignReferee(@PathVariable UUID matchId,
                                          @Valid @RequestBody AssignRefereeRequestDTO request,
                                          Authentication authentication) {
        return matchService.assignReferee(matchId, request.refereeId(), authentication.getName());
    }

    @PostMapping("/{matchId}/start")
    @PreAuthorize("hasAnyRole('REFEREE', 'ORGANIZER')")
    public MatchResponseDTO start(@PathVariable UUID matchId, Authentication authentication) {
        return matchService.startMatch(matchId, authentication.getName());
    }

    @PostMapping("/{matchId}/finish")
    @PreAuthorize("hasAnyRole('REFEREE', 'ORGANIZER')")
    public MatchResponseDTO finish(@PathVariable UUID matchId, Authentication authentication) {
        return matchService.endMatch(matchId, authentication.getName());
    }

    @PostMapping("/{matchId}/score")
    @PreAuthorize("hasAnyRole('REFEREE', 'ORGANIZER')")
    public MatchResponseDTO addScore(@PathVariable UUID matchId,
                                     @Valid @RequestBody AddScoreRequestDTO request,
                                     Authentication authentication) {
        return matchScoringService.addScore(matchId, request, authentication.getName());
    }

    @DeleteMapping("/{matchId}/score/last")
    @PreAuthorize("hasAnyRole('REFEREE', 'ORGANIZER')")
    public MatchResponseDTO undoLastScore(@PathVariable UUID matchId, Authentication authentication) {
        return matchScoringService.undoLastScore(matchId, authentication.getName());
    }

    @GetMapping("/{matchId}/events")
    public List<MatchEventResponseDTO> getEvents(@PathVariable UUID matchId) {
        return matchScoringService.getEvents(matchId);
    }
}
