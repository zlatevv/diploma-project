package com.zlatev.tournament_management_system.web.team;

import com.zlatev.tournament_management_system.dto.team.CreateTeamRequestDTO;
import com.zlatev.tournament_management_system.dto.team.TeamResponseDTO;
import com.zlatev.tournament_management_system.service.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments/{tournamentId}/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping
    public List<TeamResponseDTO> getTeams(@PathVariable UUID tournamentId) {
        return teamService.getTeams(tournamentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ORGANIZER')")
    public TeamResponseDTO addTeam(@PathVariable UUID tournamentId,
                                   @Valid @RequestBody CreateTeamRequestDTO request,
                                   Authentication authentication) {
        return teamService.addTeam(tournamentId, request, authentication.getName());
    }

    @DeleteMapping("/{teamId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ORGANIZER')")
    public void removeTeam(@PathVariable UUID tournamentId, @PathVariable UUID teamId,
                           Authentication authentication) {
        teamService.removeTeam(tournamentId, teamId, authentication.getName());
    }
}