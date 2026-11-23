package com.zlatev.tournament_management_system.web.tournament;

import com.zlatev.tournament_management_system.dto.tournament.CreateTournamentRequestDTO;
import com.zlatev.tournament_management_system.dto.tournament.TournamentResponseDTO;
import com.zlatev.tournament_management_system.service.tournament.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;

    @GetMapping
    public List<TournamentResponseDTO> getAll() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping("/{tournamentId}")
    public TournamentResponseDTO getById(@PathVariable UUID tournamentId) {
        return tournamentService.getTournamentById(tournamentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ORGANIZER')")
    public TournamentResponseDTO create(@Valid @RequestBody CreateTournamentRequestDTO request,
                                        Authentication authentication) {
        return tournamentService.createTournament(request, authentication.getName());
    }

    @PutMapping("/{tournamentId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public TournamentResponseDTO update(@PathVariable UUID tournamentId,
                                        @Valid @RequestBody CreateTournamentRequestDTO request,
                                        Authentication authentication) {
        return tournamentService.updateTournament(tournamentId, request, authentication.getName());
    }

    @DeleteMapping("/{tournamentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ORGANIZER')")
    public void delete(@PathVariable UUID tournamentId, Authentication authentication) {
        tournamentService.deleteTournament(tournamentId, authentication.getName());
    }
}