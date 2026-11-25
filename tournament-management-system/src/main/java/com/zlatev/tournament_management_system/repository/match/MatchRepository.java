package com.zlatev.tournament_management_system.repository.match;

import com.zlatev.tournament_management_system.entity.model.match.Match;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {
    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    List<Match> findByTournamentIdOrderByRoundAsc(UUID tournamentId);

    boolean existsByTournamentId(UUID tournamentId);
}
