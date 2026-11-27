package com.zlatev.tournament_management_system.mapper.tournament;

import com.zlatev.tournament_management_system.dto.tournament.CreateTournamentRequestDTO;
import com.zlatev.tournament_management_system.dto.tournament.TournamentResponseDTO;
import com.zlatev.tournament_management_system.entity.enums.tournament.TournamentStatus;
import com.zlatev.tournament_management_system.entity.model.sport.Sport;
import com.zlatev.tournament_management_system.entity.model.tournament.Tournament;
import com.zlatev.tournament_management_system.entity.model.user.User;

public class TournamentMapper {
    public static Tournament toTournament(CreateTournamentRequestDTO dto, Sport sport, User organizer) {
        return Tournament.builder()
                .name(dto.name().trim())
                .format(dto.tournamentFormat())
                .status(TournamentStatus.DRAFT)
                .sport(sport)
                .organizer(organizer)
                .build();
    }

    public static TournamentResponseDTO toResponse(Tournament t, Integer teamCount) {
        return new TournamentResponseDTO(
                t.getId(), t.getName(), t.getFormat(), t.getStatus(),
                t.getSport().getId(), t.getSport().getSportName(),
                t.getOrganizer().getEmail(), teamCount);
    }
}