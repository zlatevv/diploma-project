package com.zlatev.tournament_management_system.mapper.match;

import com.zlatev.tournament_management_system.dto.match.MatchResponseDTO;
import com.zlatev.tournament_management_system.entity.enums.match.MatchStatus;
import com.zlatev.tournament_management_system.entity.model.fixture.Fixture;
import com.zlatev.tournament_management_system.entity.model.match.Match;
import com.zlatev.tournament_management_system.entity.model.tournament.Tournament;

public class MatchMapper {
    public static Match toMatch(Fixture f, Tournament tournament) {
        return Match.builder()
                .tournament(tournament)
                .homeTeam(f.homeTeam())
                .awayTeam(f.awayTeam())
                .round(f.round())
                .homeScore(0)
                .awayScore(0)
                .status(MatchStatus.SCHEDULED)
                .group(f.group())
                .build();
    }

    public static MatchResponseDTO toResponse(Match m) {
        return new MatchResponseDTO(
                m.getId(), m.getRound(),
                m.getHomeTeam().getId(), m.getHomeTeam().getName(),
                m.getAwayTeam().getId(), m.getAwayTeam().getName(),
                m.getHomeScore(), m.getAwayScore(),
                m.getStatus(), m.getScheduledAt());
    }
}
