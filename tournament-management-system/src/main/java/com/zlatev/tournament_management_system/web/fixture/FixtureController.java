package com.zlatev.tournament_management_system.web.fixture;

import com.zlatev.tournament_management_system.dto.fixture.GenerateFixturesRequestDTO;
import com.zlatev.tournament_management_system.dto.match.MatchResponseDTO;
import com.zlatev.tournament_management_system.service.fixture.FixtureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments/{tournamentId}")
@RequiredArgsConstructor
public class FixtureController {
    private final FixtureService fixtureService;

    @PostMapping("/fixtures")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<MatchResponseDTO> generate(@PathVariable UUID tournamentId,
                                           @Valid @RequestBody(required = false) GenerateFixturesRequestDTO request,
                                           Authentication authentication) {
        return fixtureService.generateFixtures(tournamentId, request, authentication.getName());
    }

    @GetMapping("/matches")
    public List<MatchResponseDTO> getMatches(@PathVariable UUID tournamentId) {
        return fixtureService.getMatches(tournamentId);
    }
}
