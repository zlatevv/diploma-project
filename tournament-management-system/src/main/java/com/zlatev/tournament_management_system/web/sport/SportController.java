package com.zlatev.tournament_management_system.web.sport;

import com.zlatev.tournament_management_system.dto.scoring.CreateScoringTypeDTO;
import com.zlatev.tournament_management_system.dto.scoring.ScoringTypeResponseDTO;
import com.zlatev.tournament_management_system.dto.sport.CreateSportRequestDTO;
import com.zlatev.tournament_management_system.dto.sport.SportResponseDTO;
import com.zlatev.tournament_management_system.service.sport.SportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sports")
@RequiredArgsConstructor
public class SportController {

    private final SportService sportService;

    // ---------- Sports ----------

    @GetMapping
    public List<SportResponseDTO> getAllSports() {
        return sportService.getAllSports();
    }

    @GetMapping("/{sportId}")
    public SportResponseDTO getSportById(@PathVariable UUID sportId) {
        return sportService.getSportById(sportId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ORGANIZER')")
    public SportResponseDTO createSport(@Valid @RequestBody CreateSportRequestDTO request) {
        return sportService.createSport(request);
    }

    @PutMapping("/{sportId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public SportResponseDTO updateSport(@PathVariable UUID sportId,
                                        @Valid @RequestBody CreateSportRequestDTO request) {
        return sportService.updateSport(sportId, request);
    }

    @DeleteMapping("/{sportId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ORGANIZER')")
    public void deleteSport(@PathVariable UUID sportId) {
        sportService.deleteSport(sportId);
    }

    // ---------- Scoring types of a sport ----------

    @GetMapping("/{sportId}/scoring-types")
    public List<ScoringTypeResponseDTO> getScoringTypes(@PathVariable UUID sportId) {
        return sportService.getScoringTypes(sportId);
    }

    @PostMapping("/{sportId}/scoring-types")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ScoringTypeResponseDTO addScoringType(@PathVariable UUID sportId,
                                                 @Valid @RequestBody CreateScoringTypeDTO request) {
        return sportService.addScoringType(sportId, request);
    }
}