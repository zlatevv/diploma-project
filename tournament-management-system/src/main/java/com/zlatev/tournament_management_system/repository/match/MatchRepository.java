package com.zlatev.tournament_management_system.repository.match;

import com.zlatev.tournament_management_system.entity.model.match.Match;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {
    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    List<Match> findByTournamentIdOrderByRoundAsc(UUID tournamentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Match m where m.id = :id")
    Optional<Match> findByIdForUpdate(@Param("id") UUID id);
}
